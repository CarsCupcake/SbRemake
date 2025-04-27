package me.carscupcake.sbremake.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

import java.util.Arrays;
import java.util.Iterator;

public class StringUtils {
    private StringUtils() {
    }

    public static String cleanDouble(double d, int digits) {
        int dg = countDigitsAfterDecimal(d, digits);
        return String.format("%." + (Math.min(dg, digits)) + "f", d);
    }

    private static int countDigitsAfterDecimal(double number, int digits) {
        if (number % 1 == 0) return 0;
        String numberStr = Double.toString(number);
        int decimalIndex = numberStr.indexOf('.');
        if (decimalIndex == -1) {
            return 0;
        }
        int trailingZeros = 0;
        for (int i = Math.min(numberStr.length() - 1, decimalIndex + digits); i > decimalIndex; i--) {
            var char_ = numberStr.charAt(i);
            if (char_ == '0') trailingZeros++;
            else break;
        }
        return numberStr.length() - decimalIndex - 1 - trailingZeros;
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

    public static String toFormatedNumber(double number) {
        long asLong = (long) number;
        String s = toFormatedNumber(asLong);
        return s + ((number % 1 == 0) ? "" : String.valueOf(cleanDouble(number - asLong)).substring(1));
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
        builder.append(Component.text(repeat, finishedProgress));
        builder.append(Component.text(String.valueOf(piece).repeat(Math.max(0, (bars - done))), notFinishedProgress));
        return builder.build();
    }

    public static String makeProgressBarAsString(int bars, double value, double maxValue, String notFinishedProgress, String finishedProgress, String piece) {
        double pers = value / maxValue;
        if (pers > 1) pers = 1;
        if (pers < 0) pers = 0;
        int done = (int) (bars * pers);
        String repeat = String.valueOf(piece).repeat(Math.max(0, done));
        return finishedProgress + repeat + notFinishedProgress + String.valueOf(piece).repeat(Math.max(0, (bars - done)));
    }

    public static String stripeColorCodes(String s) {
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
                            if (num > 9999999999d) str = ((int) ((num / 1000000000))) + "b";
                            else str = (NumberUtil.round(num / 1000000000, 1)) + "b";
                        } else str = ((int) ((num / 1000000))) + "M";

                    } else str = (NumberUtil.round(num / 1000000, 1)) + "M";
                } else str = ((int) ((num / 1000))) + "k";
            } else str = (NumberUtil.round(num / 1000, 1)) + "k";
        } else str = (cleanDouble(num));
        return str;
    }

    public static String timeToString(int seconds) {
        int minutes = (int) (seconds / 60d);
        return (minutes) + ":" + ((int) (seconds - (minutes * 60)));
    }

    public static String ticksToString(long l) {
        double seconds = (double) l / 20d;
        int minutes = (int) (seconds / 60d);
        int displaySeconds = (int) (seconds - (minutes * 60));
        return (minutes) + ":" + (displaySeconds < 10 ? "0" : "") + (displaySeconds);
    }
}
