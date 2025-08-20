package me.carscupcake.sbremake.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public record MultiSpan<T>(List<Span<T>> spans, int size) implements List<T> {

    @Contract("_ -> new")
    public static <T> @NotNull MultiSpan<T> from(List<Span<T>> spans) {
        var size = 0;
        for (var span : spans) {
            size += span.size();
        }
        return new MultiSpan<>(Collections.unmodifiableList(spans), size);
    }

    public T set(int index, T value) {
        var i = index;
        if (index >= size) {
            throw new IndexOutOfBoundsException(i + " >= " + size());
        }
        for (var span : spans) {
            if (index < span.size()) {
                return span.set(index, value);
            }
            index -= span.size();
        }
        throw new IndexOutOfBoundsException(i + " >= " + size());
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        var i = 0;
        for (var span : spans) {
            if (span.contains(o)) return i;
            i += span.size();
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull ListIterator<T> listIterator() {
        return new MultiSpanIterator();
    }

    @Override
    public @NotNull ListIterator<T> listIterator(int index) {
        return new MultiSpanIterator(index);
    }

    @Override
    public @NotNull List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    public T get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException(index + " >= " + size());
        }
        for (var span : spans) {
            if (index < span.size()) {
                return span.get(index);
            }
            index -= span.size();
        }
        throw new IndexOutOfBoundsException(index + " >= " + size());
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new MultiSpanIterator();
    }

    public Object @NotNull [] toArray() {
        // Estimate size of array; be prepared to see more or fewer elements
        Object[] r = new Object[size()];
        Iterator<T> it = iterator();
        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) // fewer elements than expected
                return Arrays.copyOf(r, i);
            r[i] = it.next();
        }
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    @SuppressWarnings("unchecked")
    public <E> E @NotNull [] toArray(E[] a) {
        // Estimate size of array; be prepared to see more or fewer elements
        int size = size();
        E[] r = a.length >= size ? a :
                (E[])java.lang.reflect.Array
                        .newInstance(a.getClass().getComponentType(), size);
        Iterator<T> it = iterator();

        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) { // fewer elements than expected
                if (a == r) {
                    r[i] = null; // null-terminate
                } else if (a.length < i) {
                    return Arrays.copyOf(r, i);
                } else {
                    System.arraycopy(r, 0, a, 0, i);
                    if (a.length > i) {
                        a[i] = null;
                    }
                }
                return a;
            }
            r[i] = (E) it.next();
        }
        // more elements than expected
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
        throw new UnsupportedOperationException();
        /*int len = r.length;
        int i = len;
        while (it.hasNext()) {
            if (i == len) {
                len = ArraysSupport.newLength(len,
                        1,
                        (len >> 1) + 1);
                r = Arrays.copyOf(r, len);
            }
            r[i++] = (T)it.next();
        }
        // trim if overallocated
        return (i == len) ? r : Arrays.copyOf(r, i);*/
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    public class MultiSpanIterator implements ListIterator<T> {

        public MultiSpanIterator() {
        }

        public MultiSpanIterator(int index) {
            this.index = index;
        }

        private int index = 0;
        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            return get(index++);
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public T previous() {
            return get(index--);
        }

        @Override
        public int nextIndex() {
            return index + 1;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            MultiSpan.this.set(index, t);
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }
}
