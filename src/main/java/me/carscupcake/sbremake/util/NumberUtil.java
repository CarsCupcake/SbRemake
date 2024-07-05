package me.carscupcake.sbremake.util;

public class NumberUtil {
    public static double round(double num, int digit) {
        double d = Math.pow(10, digit);

        return Math.round(num * d) / d;
    }
}
