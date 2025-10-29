package me.carscupcake.sbremake.util;

public class NumberUtil {
    public static double round(double num, int digit) {
        double d = Math.pow(10, digit);

        return Math.round(num * d) / d;
    }

    public static boolean inRange(double num, int min, int max) {
        return num >= min && num <= max;
    }

    public static long fromShortenedString(String str) throws NumberFormatException {
        long mult = 1;
        if (str.endsWith("k")) {
            mult = 1_000;
            str = str.replace("k", "");
        }
        if (str.endsWith("m")) {
            mult = 1_000_000;
            str = str.replace("m", "");
        }
        if (str.endsWith("b")) {
            mult = 1_000_000_000;
            str = str.replace("b", "");
        }
        return Long.parseLong(str) * mult;
    }
}
