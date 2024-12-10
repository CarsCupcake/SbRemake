package me.carscupcake.sbremake.util;

public class NumberUtil {
    public static double round(double num, int digit) {
        double d = Math.pow(10, digit);

        return Math.round(num * d) / d;
    }
    public static boolean inRange(double num, int min, int max) {
        return num >= min && num <= max;
    }
}
