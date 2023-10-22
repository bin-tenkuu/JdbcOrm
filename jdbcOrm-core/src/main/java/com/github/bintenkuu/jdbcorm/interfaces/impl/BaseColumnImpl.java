package com.github.bintenkuu.jdbcorm.interfaces.impl;

import com.github.bintenkuu.jdbcorm.interfaces.BaseColumn;

import java.util.function.BiConsumer;

public record BaseColumnImpl<E, T>(
        String name,
        Class<T> typeClass,
        BiConsumer<E, T> setter
) implements BaseColumn<E, T> {

}
