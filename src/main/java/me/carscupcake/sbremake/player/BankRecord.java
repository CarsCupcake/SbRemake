package me.carscupcake.sbremake.player;

import me.carscupcake.sbremake.config.*;
import me.carscupcake.sbremake.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;

public record BankRecord(@ConfigParameter("sum") double sum, @ConfigParameter("time") LocalDateTime time) implements DefaultConfigItem {

    @ConfigConstructor
    public BankRecord {}

    @Override
    public @NotNull String toString() {
        return (sum >= 0 ? "§a+" : "§c-") + " §6" + StringUtils.toFormatedNumber(Math.abs(sum)) + " §e" + formatTime();
    }

    private String formatTime() {
        var duration = Duration.between(time, LocalDateTime.now());
        if (duration.toDays() > 0) {
            return duration.toDays() +  " days";
        }
        if (duration.toHours() > 0) {
            return duration.toHours() +  " hours";
        }
        if (duration.toMinutes() > 0) {
            return duration.toMinutes() +  " minutes";
        }
        return duration.toSeconds() + " seconds";
    }
}
