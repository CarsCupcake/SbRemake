package me.carscupcake.sbremake.util;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.adventure.provider.MinestomComponentLoggerProvider;
import org.apache.log4j.helpers.ISO8601DateFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.simple.SimpleLogger;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Getter
public class SkyblockSimpleLogger extends SimpleLogger implements ComponentLogger {
    private static final LegacyComponentSerializer SERIALIZER;

    static {
        try {
            Field f = MinestomComponentLoggerProvider.class.getDeclaredField("SERIALIZER");
            f.setAccessible(true);
            SERIALIZER = (LegacyComponentSerializer) f.get(null);
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
                        var myFormatObj = new ISO8601DateFormat();
                        zip(logFolder, List.of(logs, errors), myFormatObj.format(new Date(attr.creationTime().toMillis())));
                        success = true;
                    }
                } catch (IOException ignored) {
                }
                if (!success) {
                    var myFormatObj = new ISO8601DateFormat();
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

    public static File zip(File folder, List<File> files, String filename) {
        File zipfile = new File(folder, filename.replace(' ', '_').replace(':', '.') + ".zip");
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile))) {
            for (File file : files) {
                try(FileInputStream in = new FileInputStream(file.getCanonicalFile())) {
                    // add ZIP entry to output stream
                    out.putNextEntry(new ZipEntry(file.getName()));
                    // transfer bytes from the file to the ZIP file
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    // complete the entry
                    out.closeEntry();
                }
            }
            return zipfile;
        } catch (IOException ex) {
            throw new  RuntimeException(ex);
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
}
