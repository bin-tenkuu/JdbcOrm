package com.github.bintenkuu.jdbcorm.interfaces;

import lombok.val;

import java.util.function.Supplier;

/**
 * @author bin
 * @since 2023/08/31
 */
public record SampleTable<E>(
        Class<E> clazz
) {

    public Supplier<E> newer() {
        val constructor = clazz.getConstructors()[0];
        return null;
    }
}
