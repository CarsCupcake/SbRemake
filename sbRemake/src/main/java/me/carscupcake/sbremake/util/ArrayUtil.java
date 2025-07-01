package me.carscupcake.sbremake.util;

public class ArrayUtil {
    public static <T> int indexOf(T[] array, T object) {
        int i = 0;
        for (T t : array) {
            if (t.equals(object)) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
