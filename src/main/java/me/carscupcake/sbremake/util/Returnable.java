package me.carscupcake.sbremake.util;

import net.minestom.server.utils.Unit;

import java.util.function.Function;

public interface Returnable<T> extends Function<Unit, T> {
    @Override
    default T apply(Unit unit) {
        return get();
    }

    T get();
}
