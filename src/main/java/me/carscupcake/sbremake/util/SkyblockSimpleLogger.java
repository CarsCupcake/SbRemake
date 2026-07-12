package me.carscupcake.sbremake.util;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.logger.slf4j.ComponentLoggingEventBuilder;
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer;
import net.kyori.adventure.util.ComponentMessageThrowable;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.adventure.provider.MinestomComponentLoggerProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.event.LoggingEvent;
import org.slf4j.helpers.CheckReturnValue;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.simple.SimpleLogger;
import org.slf4j.spi.DefaultLoggingEventBuilder;
import org.slf4j.spi.LoggingEventAware;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Getter
public class SkyblockSimpleLogger extends SimpleLogger implements ComponentLogger {
    private static final ANSIComponentSerializer SERIALIZER;

    static {
        try {
            Field f = MinestomComponentLoggerProvider.class.getDeclaredField("SERIALIZER");
            f.setAccessible(true);
            SERIALIZER = (ANSIComponentSerializer) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final Writer logWriter;
    private final Writer errorWriter;

    public SkyblockSimpleLogger() {
        super("Skyblock");
        var logFolder = new File("logs");
        if (!logFolder.exists()) {
            logFolder.mkdir();
        }
        var logs = new File(logFolder, "latest.txt");
        var errors = new File(logFolder, "errorLog.txt");
        if (Objects.requireNonNull(logFolder.listFiles()).length != 0) {
            if (logs.exists() && errors.exists()) {
                boolean success = false;
                try {
                    BasicFileAttributes attr = Files.readAttributes(logs.toPath(), BasicFileAttributes.class);
                    if (attr.creationTime().toMillis() != 0) {
                        var myFormatObj = new StdDateFormat();
                        zip(logFolder, List.of(logs, errors), myFormatObj.format(new Date(attr.creationTime().toMillis())));
                        success = true;
                    }
                } catch (IOException ignored) {
                }
                if (!success) {
                    var myFormatObj = new StdDateFormat();
                    zip(logFolder, List.of(logs, errors), myFormatObj.format(new Date()));
                }
            }
            logs.delete();
            errors.delete();
        }
        try {
            logWriter = new FileWriter(logs, true);
            errorWriter = new FileWriter(errors, true);
        } catch (Exception e) {
            throw new RuntimeException("An Error occured while initializing logger!", e);
        }
    }

    public static void zip(File folder, List<File> files, String filename) {
        File zipfile = new File(folder, filename.replace(' ', '_').replace(':', '.') + ".zip");
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile))) {
            for (File file : files) {
                try (FileInputStream in = new FileInputStream(file.getCanonicalFile())) {
                    // add ZIP entrance to output stream
                    out.putNextEntry(new ZipEntry(file.getName()));
                    // transfer bytes from the file to the ZIP file
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    // complete the entrance
                    out.closeEntry();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setLogLevel(int level) {
        currentLogLevel = level;
    }

    @Override
    public void trace(String msg, Throwable throwable) {
        super.trace(msg, throwable);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        super.trace(marker, msg, t);
    }

    private void writeToLogs(Throwable t) {
        if (t != null) {
            Audiences.players().sendMessage(Component.text("§c[ERROR] " + (t)));
            try {
                errorWriter.write("ERROR: " + t + "\n");
                logWriter.write("ERROR: " + t + "\n");
                for (StackTraceElement element : t.getStackTrace()) {
                    Audiences.players().sendMessage(Component.text("§cat " + (element)));
                    errorWriter.write("at " + element + "\n");
                    logWriter.write("at " + element + "\n");
                }
                errorWriter.flush();
                logWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        super.error(msg, t);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        super.error(marker, msg, t);
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String messagePattern, Object[] arguments, Throwable throwable) {
        if (logWriter != null) {
            try {
                logWriter.write("[" + level + "] " + MessageFormatter.basicArrayFormat(messagePattern, arguments) + "\n");
                logWriter.flush();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (throwable != null) {
            writeToLogs(throwable);
        }
        super.handleNormalizedLoggingCall(level, marker, messagePattern, arguments, throwable);
    }

    @Override
    public void trace(@NotNull Component msg) {
        trace(SERIALIZER.serialize(msg));
    }

    @Override
    public void trace(@NotNull Component format, @Nullable Object arg) {
        trace(SERIALIZER.serialize(format), arg);
    }

    @Override
    public void trace(@NotNull Component format, @Nullable Object arg1, @Nullable Object arg2) {
        trace(SERIALIZER.serialize(format), arg1, arg2);
    }

    @Override
    public void trace(@NotNull Component format, @Nullable Object @NotNull ... arguments) {
        trace(SERIALIZER.serialize(format), arguments);
    }

    @Override
    public void trace(@NotNull Component msg, @Nullable Throwable t) {
        trace(SERIALIZER.serialize(msg), t);
    }

    @Override
    public void trace(@NotNull Marker marker, @NotNull Component msg) {
        super.trace(marker, SERIALIZER.serialize(msg));
    }

    @Override
    public void trace(@NotNull Marker marker, @NotNull Component format, @Nullable Object arg) {
        super.trace(marker, SERIALIZER.serialize(format), arg);
    }

    @Override
    public void trace(@NotNull Marker marker, @NotNull Component format, @Nullable Object arg1, @Nullable Object arg2) {
        super.trace(marker, SERIALIZER.serialize(format), arg1, arg2);
    }

    @Override
    public void trace(@NotNull Marker marker, @NotNull Component format, @Nullable Object @NotNull ... argArray) {
        super.trace(marker, SERIALIZER.serialize(format), argArray);
    }

    @Override
    public void trace(@NotNull Marker marker, @NotNull Component msg, @Nullable Throwable t) {
        super.trace(marker, SERIALIZER.serialize(msg), t);
    }

    @Override
    public void debug(@NotNull Component msg) {
        debug(SERIALIZER.serialize(msg));
    }

    @Override
    public void debug(@NotNull Component format, @Nullable Object arg) {
        debug(SERIALIZER.serialize(format), arg);
    }

    @Override
    public void debug(@NotNull Component format, @Nullable Object arg1, @Nullable Object arg2) {
        debug(SERIALIZER.serialize(format), arg1, arg2);
    }

    @Override
    public void debug(@NotNull Component format, @Nullable Object @NotNull ... arguments) {
        debug(SERIALIZER.serialize(format), arguments);
    }

    @Override
    public void debug(@NotNull Component msg, @Nullable Throwable t) {
        debug(SERIALIZER.serialize(msg), t);
    }

    @Override
    public void debug(@NotNull Marker marker, @NotNull Component msg) {
        debug(marker, SERIALIZER.serialize(msg));
    }

    @Override
    public void debug(@NotNull Marker marker, @NotNull Component format, @Nullable Object arg) {
        debug(marker, SERIALIZER.serialize(format), arg);
    }

    @Override
    public void debug(@NotNull Marker marker, @NotNull Component format, @Nullable Object arg1, @Nullable Object arg2) {
        debug(marker, SERIALIZER.serialize(format), arg1, arg2);
    }

    @Override
    public void debug(@NotNull Marker marker, @NotNull Component format, @Nullable Object @NotNull ... arguments) {
        debug(marker, SERIALIZER.serialize(format), arguments);
    }

    @Override
    public void debug(@NotNull Marker marker, @NotNull Component msg, @Nullable Throwable t) {
        debug(marker, SERIALIZER.serialize(msg), t);
    }

    @Override
    public void info(@NotNull Component msg) {
        info(SERIALIZER.serialize(msg));
    }

    @Override
    public void info(@NotNull Component format, @Nullable Object arg) {
        info(SERIALIZER.serialize(format), arg);
    }

    @Override
    public void info(@NotNull Component format, @Nullable Object arg1, @Nullable Object arg2) {
        info(SERIALIZER.serialize(format), arg1, arg2);
    }

    @Override
    public void info(@NotNull Component format, @Nullable Object @NotNull ... arguments) {
        info(SERIALIZER.serialize(format), arguments);

    }

    @Override
    public void info(@NotNull Component msg, @Nullable Throwable t) {
        info(SERIALIZER.serialize(msg), t);
    }

    @Override
    public void info(@NotNull Marker marker, @NotNull Component msg) {
        info(marker, SERIALIZER.serialize(msg));
    }

    @Override
    public void info(@NotNull Marker marker, @NotNull Component format, @Nullable Object arg) {
        info(marker, SERIALIZER.serialize(format), arg);

    }

    @Override
    public void info(@NotNull Marker marker, @NotNull Component format, @Nullable Object arg1, @Nullable Object arg2) {
        info(marker, SERIALIZER.serialize(format), arg1, arg2);
    }

    @Override
    public void info(@NotNull Marker marker, @NotNull Component format, @Nullable Object @NotNull ... arguments) {
        info(marker, SERIALIZER.serialize(format), arguments);
    }

    @Override
    public void info(@NotNull Marker marker, @NotNull Component msg, @NotNull Throwable t) {
        info(marker, SERIALIZER.serialize(msg), t);
    }

    @Override
    public void warn(@NotNull Component msg) {
        warn(SERIALIZER.serialize(msg));
    }

    @Override
    public void warn(@NotNull Component format, @Nullable Object arg) {
        warn(SERIALIZER.serialize(format), arg);
    }

    @Override
    public void warn(@NotNull Component format, @Nullable Object arg1, @Nullable Object arg2) {
        warn(SERIALIZER.serialize(format), arg1, arg2);
    }

    @Override
    public void warn(@NotNull Component format, @Nullable Object @NotNull ... arguments) {
        warn(SERIALIZER.serialize(format), arguments);

    }

    @Override
    public void warn(@NotNull Component msg, @Nullable Throwable t) {
        warn(SERIALIZER.serialize(msg), t);
    }

    @Override
    public void warn(@NotNull Marker marker, @NotNull Component msg) {
        warn(marker, SERIALIZER.serialize(msg));
    }

    @Override
    public void warn(@NotNull Marker marker, @NotNull Component format, @Nullable Object arg) {
        warn(marker, SERIALIZER.serialize(format), arg);

    }

    @Override
    public void warn(@NotNull Marker marker, @NotNull Component format, @Nullable Object arg1, @Nullable Object arg2) {
        warn(marker, SERIALIZER.serialize(format), arg1, arg2);
    }

    @Override
    public void warn(@NotNull Marker marker, @NotNull Component format, @Nullable Object @NotNull ... arguments) {
        warn(marker, SERIALIZER.serialize(format), arguments);
    }

    @Override
    public void warn(@NotNull Marker marker, @NotNull Component msg, @NotNull Throwable t) {
        warn(marker, SERIALIZER.serialize(msg), t);
    }

    @Override
    public void error(@NotNull Component msg) {
        error(SERIALIZER.serialize(msg));
    }

    @Override
    public void error(@NotNull Component format, @Nullable Object arg) {
        error(SERIALIZER.serialize(format), arg);
    }

    @Override
    public void error(@NotNull Component format, @Nullable Object arg1, @Nullable Object arg2) {
        error(SERIALIZER.serialize(format), arg1, arg2);
    }

    @Override
    public void error(@NotNull Component format, @Nullable Object @NotNull ... arguments) {
        error(SERIALIZER.serialize(format), arguments);

    }

    @Override
    public void error(@NotNull Component msg, @Nullable Throwable t) {
        error(SERIALIZER.serialize(msg), t);
    }

    @Override
    public void error(@NotNull Marker marker, @NotNull Component msg) {
        error(marker, SERIALIZER.serialize(msg));
    }

    @Override
    public void error(@NotNull Marker marker, @NotNull Component format, @Nullable Object arg) {
        error(marker, SERIALIZER.serialize(format), arg);

    }

    @Override
    public void error(@NotNull Marker marker, @NotNull Component format, @Nullable Object arg1, @Nullable Object arg2) {
        error(marker, SERIALIZER.serialize(format), arg1, arg2);
    }

    @Override
    public void error(@NotNull Marker marker, @NotNull Component format, @Nullable Object @NotNull ... arguments) {
        error(marker, SERIALIZER.serialize(format), arguments);
    }

    @Override
    public void error(@NotNull Marker marker, @NotNull Component msg, @NotNull Throwable t) {
        error(marker, SERIALIZER.serialize(msg), t);
    }

    @Override
    public @NonNull ComponentLoggingEventBuilder makeLoggingEventBuilder(@NonNull Level level) {
        return new ComponentLoggingEventBuilderImpl(this, level, _ -> "NOOP!");
    }

    @Override
    public @NonNull ComponentLoggingEventBuilder atLevel(@NonNull Level level) {
        return new ComponentLoggingEventBuilderImpl(this, level, _ -> "NOOP!");
    }


    static class ComponentLoggingEventBuilderImpl extends DefaultLoggingEventBuilder implements ComponentLoggingEventBuilder {

        private static final String FQCN = ComponentLoggingEventBuilderImpl.class.getName();

        private final Function<Component, String> serializer;

        ComponentLoggingEventBuilderImpl(final Logger logger, final Level level, final Function<Component, String> serializer) {
            super(logger, level);
            this.serializer = serializer;
        }

        private @org.jspecify.annotations.Nullable String serialize(final @org.jspecify.annotations.Nullable ComponentLike component) {
            return component == null ? null : this.serializer.apply(component.asComponent());
        }

        private @org.jspecify.annotations.Nullable Object maybeSerialize(final @org.jspecify.annotations.Nullable Object input) {
            if (input instanceof ComponentLike componentLike) {
                return this.serialize(componentLike.asComponent());
            } else {
                return input;
            }
        }

        private @org.jspecify.annotations.Nullable Object[] maybeSerialize(final @org.jspecify.annotations.Nullable Object... args) {
            @org.jspecify.annotations.Nullable Object[] writable = args;
            for (int i = 0; i < writable.length; i++) {
                if (writable[i] instanceof ComponentLike) {
                    if (writable == args) {
                        writable = Arrays.copyOf(args, args.length);
                    }
                    writable[i] = this.serialize(((ComponentLike) writable[i]).asComponent());
                }
            }

            if (writable.length > 0 && writable[writable.length - 1] instanceof Throwable) {
                if (writable == args) {
                    writable = Arrays.copyOf(args, args.length);
                }
                writable[writable.length - 1] = UnpackedComponentThrowable.unpack((Throwable) writable[writable.length - 1], this.serializer);
            }

            return writable;
        }

        @Override
        public @NonNull ComponentLoggingEventBuilder setMessage(final @org.jspecify.annotations.Nullable ComponentLike message) {
            super.setMessage(this.serialize(message));
            return this;
        }

        @Override
        public @NonNull ComponentLoggingEventBuilder setComponentMessage(final Supplier<? extends ComponentLike> messageSupplier) {
            super.setMessage(this.serialize(messageSupplier.get()));
            return this;
        }

        @Override
        public void log(final @org.jspecify.annotations.Nullable ComponentLike message) {
            this.loggingEvent.setMessage(this.serialize(message));
            this.logOwnBoundary(this.loggingEvent);
        }

        /**
         * Set the message with one argument and publish this logging event.
         *
         * @param message the message
         * @param arg     the argument
         * @see #log()
         * @since 4.12.0
         */
        @Override
        public void log(final @org.jspecify.annotations.Nullable ComponentLike message, final @org.jspecify.annotations.Nullable Object arg) {
            this.loggingEvent.setMessage(this.serialize(message));
            this.loggingEvent.addArgument(this.maybeSerialize(arg));
            this.logOwnBoundary(this.loggingEvent);
        }

        /**
         * Set the message with two arguments and publish this logging event.
         *
         * @param message the message
         * @param arg0    the first argument
         * @param arg1    the second argument
         * @see #log()
         * @since 4.12.0
         */
        @Override
        public void log(final @org.jspecify.annotations.Nullable ComponentLike message, final @org.jspecify.annotations.Nullable Object arg0, final @org.jspecify.annotations.Nullable Object arg1) {
            this.loggingEvent.setMessage(this.serialize(message));
            this.loggingEvent.addArgument(this.maybeSerialize(arg0));
            this.loggingEvent.addArgument(this.maybeSerialize(arg1));
            this.logOwnBoundary(this.loggingEvent);
        }

        /**
         * Set the message with an array of arguments and publish this logging event.
         *
         * @param message the message
         * @param args    the arguments
         * @see #log()
         * @since 4.12.0
         */
        @Override
        public void log(final @org.jspecify.annotations.Nullable ComponentLike message, final Object @NonNull ... args) {
            this.loggingEvent.setMessage(this.serialize(message));
            this.loggingEvent.addArguments(this.maybeSerialize(args));
            this.logOwnBoundary(this.loggingEvent);
        }

        /**
         * Set the message supplier and publish this logging event.
         *
         * @param messageSupplier the message supplier
         * @see #log()
         * @since 4.12.0
         */
        @Override
        public void logComponent(final Supplier<? extends ComponentLike> messageSupplier) {
            this.log(messageSupplier.get());
        }

        // Impl junk

        private void logOwnBoundary(final LoggingEvent event) {
            this.setCallerBoundary(FQCN);
            if (this.logger instanceof LoggingEventAware) {
                ((LoggingEventAware) this.logger).log(event);
            } else {
                super.log(event);
            }
        }

        // Overloads for direct-log methods
        @Override
        public void log(final String message, final Object arg) {
            this.loggingEvent.setMessage(message);
            this.loggingEvent.addArgument(this.maybeSerialize(arg));
            this.logOwnBoundary(this.loggingEvent);
        }

        @Override
        public void log(final String message, final Object arg0, final Object arg1) {
            this.loggingEvent.setMessage(message);
            this.loggingEvent.addArgument(this.maybeSerialize(arg0));
            this.loggingEvent.addArgument(this.maybeSerialize(arg1));
            this.logOwnBoundary(this.loggingEvent);
        }

        @Override
        public void log(final String message, final Object... args) {
            this.loggingEvent.setMessage(message);
            this.loggingEvent.addArguments(this.maybeSerialize(args));
            this.logOwnBoundary(this.loggingEvent);
        }

        // Overloads for return types
        @Override
        @CheckReturnValue
        public @NonNull ComponentLoggingEventBuilder setCause(final @org.jspecify.annotations.Nullable Throwable cause) {
            super.setCause(UnpackedComponentThrowable.unpack(cause, this.serializer));
            return this;
        }

        @Override
        @CheckReturnValue
        public @NonNull ComponentLoggingEventBuilder addMarker(final @NonNull Marker marker) {
            super.addMarker(marker);
            return this;
        }

        @Override
        @CheckReturnValue
        public @NonNull ComponentLoggingEventBuilder addArgument(final @org.jspecify.annotations.Nullable Object p) {
            super.addArgument(this.maybeSerialize(p));
            return this;
        }

        @Override
        @CheckReturnValue
        public @NonNull ComponentLoggingEventBuilder addArgument(final @org.jspecify.annotations.Nullable Supplier<?> objectSupplier) {
            super.addArgument(objectSupplier == null ? null : this.maybeSerialize(objectSupplier.get()));
            return this;
        }

        @Override
        @CheckReturnValue
        public @NonNull ComponentLoggingEventBuilder addKeyValue(final @org.jspecify.annotations.Nullable String key, final @org.jspecify.annotations.Nullable Object value) {
            super.addKeyValue(key, this.maybeSerialize(value));
            return this;
        }

        @Override
        @CheckReturnValue
        public @NonNull ComponentLoggingEventBuilder addKeyValue(final @org.jspecify.annotations.Nullable String key, final Supplier<Object> valueSupplier) {
            super.addKeyValue(key, this.maybeSerialize(valueSupplier.get()));
            return this;
        }

        @Override
        @CheckReturnValue
        public @NonNull ComponentLoggingEventBuilder setMessage(final @org.jspecify.annotations.Nullable String message) {
            super.setMessage(message);
            return this;
        }

        @Override
        @CheckReturnValue
        public @NonNull ComponentLoggingEventBuilder setMessage(final @org.jspecify.annotations.Nullable Supplier<String> messageSupplier) {
            super.setMessage(messageSupplier);
            return this;
        }
    }

    static  class UnpackedComponentThrowable extends Throwable {
        @Serial private static final long serialVersionUID = -1L;

        private final Class<? extends Throwable> backingType;

        static Throwable unpack(final Throwable maybeRich, final Function<Component, String> serializer) {
            if (!(maybeRich instanceof final ComponentMessageThrowable cmt)) return maybeRich; // TODO: do we need to unwrap any nested exceptions?

            final Component message = cmt.componentMessage();
            final Throwable cause = maybeRich.getCause() != null ? unpack(maybeRich.getCause(), serializer) : null;
            final Throwable[] suppressed = maybeRich.getSuppressed();

            final UnpackedComponentThrowable ret = new UnpackedComponentThrowable(maybeRich.getClass(), serializer.apply(message), cause);
            ret.setStackTrace(maybeRich.getStackTrace());
            for (final Throwable throwable : suppressed) {
                ret.addSuppressed(unpack(throwable, serializer));
            }

            return ret;
        }

        private UnpackedComponentThrowable(final Class<? extends Throwable> backingType, final String serializedMessage, final Throwable cause) {
            super(serializedMessage, cause);
            this.backingType = backingType;
        }

        @Override
        public String toString() {
            final String className = this.backingType.getName();
            final String message = this.getMessage();
            return message == null ? className : className + ":" + message;
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
