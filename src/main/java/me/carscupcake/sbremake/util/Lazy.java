package me.carscupcake.sbremake.util;

import java.util.function.Supplier;

public class Lazy<T> {
    private final boolean mutable;
    private boolean initialized = false;
    private T value;
    private final Supplier<T> supplier;

    public Lazy(Supplier<T> supplier, boolean mutable) {
        this.supplier = supplier;
        this.mutable = mutable;
    }

    public Lazy(Supplier<T> supplier) {
        this(supplier, false);
    }

    public T get() {
        if (!initialized) {
            value = supplier.get();
        }
        return value;
    }

    public void set(T value) {
        if (!mutable) {
            throw new IllegalStateException("Not mutable!");
        }
        if (!initialized) {
            initialized = true;
        }
        this.value = value;
    }
}
