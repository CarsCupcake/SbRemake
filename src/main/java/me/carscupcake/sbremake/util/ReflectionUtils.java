package me.carscupcake.sbremake.util;

import org.reflections.Reflections;

import java.util.function.Consumer;

public class ReflectionUtils {
    public static <T> void forEachClassIn(String pack, Class<T> type, Consumer<? super Class<? extends T>> t) {
        Reflections reflections = new Reflections(pack);
        reflections.getSubTypesOf(type).forEach(t);
    }
}
