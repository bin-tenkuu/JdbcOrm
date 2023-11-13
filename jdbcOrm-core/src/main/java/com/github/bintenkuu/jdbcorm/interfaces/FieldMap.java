package com.github.bintenkuu.jdbcorm.interfaces;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class FieldMap {
    private final HashMap<BaseColumn<?, ?>, Object> map;

    public FieldMap(int size) {
        map = new HashMap<>(size);
    }

    public int size() {
        return map.size();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T get(BaseColumn<?, T> key) {
        return (T) map.get(key);
    }

    public <T> void put(BaseColumn<?, T> key, T value) {
        map.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T remove(BaseColumn<?, T> key) {
        return (T) map.remove(key);
    }

    public void clear() {
        map.clear();
    }
}
