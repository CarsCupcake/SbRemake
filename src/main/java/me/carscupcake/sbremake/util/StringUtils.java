package me.carscupcake.sbremake.util;

import java.util.Arrays;
import java.util.Iterator;

public class StringUtils {
    private StringUtils(){}
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
        while(stringIterator.hasNext()) {
            String s = stringIterator.next();
            if (startCapital) {
                builder.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
            } else builder.append(s);
            if (stringIterator.hasNext()) builder.append(" ");
        }
        return builder.toString();
    }
}
