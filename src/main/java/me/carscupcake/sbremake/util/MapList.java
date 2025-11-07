package me.carscupcake.sbremake.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MapList<T, K> extends HashMap<T, List<K>> {

    public void add(T key, K value) {
        List<K> ks;
        if (super.containsKey(key)) {
            ks = super.get(key);
        } else ks = new LinkedList<>();
        ks.add(value);
        put(key, ks);
    }

    public void removeFromList(T key, K value) {
        List<K> ks = super.get(key);
        ks.remove(value);
        if (ks.isEmpty()) super.remove(key);
        else super.put(key, ks);
    }

    @Override
    public List<K> get(Object key) {
        List<K> k = super.get(key);
        if (k == null) k = new LinkedList<>();
        return k;
    }

    public K get(Object key, int index) {
        return get(key).get(index);
    }

    public static class Builder<T, K> {
        private final MapList<T, K> mapList = new MapList<>();

        public MapList<T, K> build() {
            return mapList;
        }

        public Builder<T, K> add(T t, K k) {
            mapList.add(t, k);
            return this;
        }
    }

    public static <T, K> Builder<T, K> builder() {
        return new Builder<>();
    }
}
