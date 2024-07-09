package me.carscupcake.sbremake.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;

public class StringUtils {
    private StringUtils() {
    }

    public static String cleanDouble(double d, int digits) {
        return String.format(STR."%.\{(d % 1d == 0) ? 0 : digits}f", d);
    }

    public static String cleanDouble(double d) {
        return cleanDouble(d, 2);
    }

    public static String connect(String[] strings) {
        return connect(strings, false);
    }

    public static String connect(String[] strings, boolean startCapital) {
        Iterator<String> stringIterator = Arrays.stream(strings).iterator();
        StringBuilder builder = new StringBuilder();
        while (stringIterator.hasNext()) {
            String s = stringIterator.next();
            if (startCapital) {
                builder.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
            } else builder.append(s);
            if (stringIterator.hasNext()) builder.append(" ");
        }
        return builder.toString();
    }

    public static String toFormatedNumber(int number) {
        String str = String.valueOf(number);
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        str = sb.toString();
        StringBuilder newString = new StringBuilder();
        int digitRunner = 0;
        for (int i = 0; i < str.toCharArray().length; i++) {
            newString.append(str.toCharArray()[i]);
            digitRunner++;
            if (digitRunner == 3 && (i + 1) != str.toCharArray().length) {
                digitRunner = 0;
                newString.append(",");
            }
        }
        return newString.reverse().toString();
    }
    public static String toFormatedNumber(long number) {
        String str = String.valueOf(number);
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        str = sb.toString();
        StringBuilder newString = new StringBuilder();
        int digitRunner = 0;
        for (int i = 0; i < str.toCharArray().length; i++) {
            newString.append(str.toCharArray()[i]);
            digitRunner++;
            if (digitRunner == 3 && (i + 1) != str.toCharArray().length) {
                digitRunner = 0;
                newString.append(",");
            }
        }
        return newString.reverse().toString();
    }

    public static String toRoman(int num) {
        if (num == 0) return "0";

        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanLetters = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num = num - values[i];
                roman.append(romanLetters[i]);
            }
        }
        return roman.toString();
    }

    public static TextComponent makeProgressBar(int bars, double value, double maxValue, TextColor notFinishedProgress, TextColor finishedProgress) {
        return makeProgressBar(bars, value, maxValue, notFinishedProgress, finishedProgress, "-");
    }

    public static TextComponent makeProgressBar(int bars, double value, double maxValue, TextColor notFinishedProgress, TextColor finishedProgress, String piece) {
        double pers = value / maxValue;
        if (pers > 1) pers = 1;
        if (pers < 0) pers = 0;
        int done = (int) (bars * pers);
        TextComponent.Builder builder = Component.text();
        String repeat = String.valueOf(piece).repeat(Math.max(0, done));
        if (done != 0) {
            builder.append(Component.text(repeat, finishedProgress));
        }
        if (bars - done != 0) {
            builder.append(Component.text(repeat, finishedProgress));
            builder.append(Component.text(String.valueOf(piece).repeat(Math.max(0, (bars - done))), notFinishedProgress));
        }
        return builder.build();
    }

    public static String stripeColorCodes(String s){
        StringBuilder name = new StringBuilder();
        if (s.contains("§"))
            for (String split : s.split("§")) {
                if (split.isEmpty()) continue;
                name.append(split.substring(1));
            }
        else
            name.append(s);
        return name.toString();
    }

    @SuppressWarnings("preview")
    public static String toShortNumber(double num) {
        String str;
        if (num > 999) {
            if (num > 9999) {
                if (num > 999999) {
                    if (num > 9999999) {
                        if (num > 999999999d) {
                            if (num > 9999999999d) str = STR."\{(int) ((num / 1000000000))}b";
                            else str = STR."\{NumberUtil.round(num / 1000000000, 1)}b";
                        } else str = STR."\{(int) ((num / 1000000))}M";

                    } else str = STR."\{NumberUtil.round(num / 1000000, 1)}M";
                } else str = STR."\{(int) ((num / 1000))}k";
            } else str = STR."\{NumberUtil.round(num / 1000, 1)}k";
        } else str = STR."\{cleanDouble(num)}";
        return str;
    }
}
