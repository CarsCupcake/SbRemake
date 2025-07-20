package me.carscupcake.sbremake.util;

import java.util.LinkedList;

public class LimitedList<E> extends LinkedList<E> {
    private final int maxElements;

    public LimitedList(int maxElements) {
        this.maxElements = maxElements;
    }

    @Override
    public boolean add(E e) {
        if(this.size() > maxElements - 1) {
            this.remove(0);
        }
        return super.add(e);
    }
}