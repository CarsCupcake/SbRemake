package me.carscupcake.sbremake.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.adventure.provider.MinestomComponentLoggerProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.simple.SimpleLogger;

import java.lang.reflect.Field;

@SuppressWarnings("preview")
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
    
    public SkyblockSimpleLogger() {
        super("Skyblock");
    }

    @Override
    public void trace(String msg, Throwable throwable) {
        if (throwable != null) {
            Audiences.players().sendMessage(Component.text("§c[ERROR] " + (throwable) ));
            for (StackTraceElement element : throwable.getStackTrace()) {
                Audiences.players().sendMessage(Component.text("§cat " + (element) ));
            }
        }
        super.trace(msg, throwable);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        if (t != null) {
            Audiences.players().sendMessage(Component.text("§c[ERROR] " + (t) ));
            for (StackTraceElement element : t.getStackTrace()) {
                Audiences.players().sendMessage(Component.text("§cat " + (element) ));
            }
        }
        super.trace(marker, msg, t);
    }

    @Override
    public void error(String msg, Throwable t) {
        if (t != null) {
            Audiences.players().sendMessage(Component.text("§c[ERROR] " + (t) ));
            for (StackTraceElement element : t.getStackTrace()) {
                Audiences.players().sendMessage(Component.text("§cat " + (element) ));
            }
        }
        super.error(msg, t);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        if (t != null) {
            Audiences.players().sendMessage(Component.text("§c[ERROR] " + (t) ));
            for (StackTraceElement element : t.getStackTrace()) {
                Audiences.players().sendMessage(Component.text("§cat " + (element) ));
            }
        }
        super.error(marker, msg, t);
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String messagePattern, Object[] arguments, Throwable throwable) {
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
        info( SERIALIZER.serialize(format), arg);
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
        warn( SERIALIZER.serialize(format), arg);
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
        error( SERIALIZER.serialize(format), arg);
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
