package me.carscupcake.sbremake.worlds;

import lombok.Getter;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.util.ArrayUtil;
import me.carscupcake.sbremake.util.TaskScheduler;

public class Time {
    public static int year = 0;
    public static int day = 0;
    public static int hour = 0;
    public static int minute = 0;
    public static Season season;
    public static long tick;

    public static void init() {
        ConfigFile file = new ConfigFile("serverConfig");
        year = file.get("year", ConfigSection.INTEGER, 0);
        day = file.get("day", ConfigSection.INTEGER, 0);
        hour = file.get("hour", ConfigSection.INTEGER, 0);
        minute = file.get("minute", ConfigSection.INTEGER, 0);
        season = Season.valueOf(file.get("season", ConfigSection.STRING, Season.EarlySpring.name()));
        if (hour < 6) {
            tick = 24000 - (hour * 1000L + (long) (minute * (1000d / 60)));
        } else {
            tick = ((hour - 6) * 1000L + (long) (minute * (1000d / 60)));
        }
        new TaskScheduler() {
            @Override
            public void run() {
                tick++;
                if (tick % 166 == 0) {
                    if (minute == 50) {
                        if (hour == 23) {
                            hour = 0;
                            tick = 0;
                            if (day == 31) {
                                day = 1;
                                int index = ArrayUtil.indexOf(Season.values(), season);
                                if (index == Season.values().length) {
                                    year++;
                                    season = Season.EarlySpring;
                                }
                                season = Season.values()[index];
                            } else day++;
                        } else hour++;
                        minute = 0;
                    } else minute += 10;
                }
            }
        }.repeatTask(1, 1);
    }

    public static String getTimeSuffix() {
        return hour > 12 ? "pm" : "am";
    }

    public static String timeToString() {
        return (hour > 12 ? (hour - 12) : hour) + ":" + (minute < 10 ? "00" : minute) + (getTimeSuffix());
    }

    public static void save() {
        ConfigFile file = new ConfigFile("serverConfig");
        file.set("year", year, ConfigSection.INTEGER);
        file.set("day", day, ConfigSection.INTEGER);
        file.set("hour", hour, ConfigSection.INTEGER);
        file.set("minute", minute, ConfigSection.INTEGER);
        file.set("season", season.name(), ConfigSection.STRING);
        file.save();
    }

    @Getter
    public enum Season {
        EarlySpring,
        Spring,
        LateSpring,
        EarlySummer,
        Summer,
        LateSummer,
        EarlyAutumn,
        Autumn,
        LateAutumn,
        EarlyWinter,
        Winter,
        LateWinter;

        private final String name;

        Season() {
            StringBuilder builder = new StringBuilder();
            for (char c : name().toCharArray()) {
                if (Character.isUpperCase(c) && !builder.isEmpty()) {
                    builder.append(" ");
                }
                builder.append(c);
            }
            this.name = builder.toString();
        }
    }
}
