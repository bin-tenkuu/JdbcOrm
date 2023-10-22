package com.github.bintenkuu.jdbcorm.interfaces.impl;

import com.github.bintenkuu.jdbcorm.interfaces.BaseColumn;
import com.github.bintenkuu.jdbcorm.interfaces.BaseTable;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author bin
 * @since 2023/08/31
 */
public record BaseTableImpl<E>(
        Supplier<E> newer,
        Map<String, ? extends BaseColumn<? super E, ?>> columns
) implements BaseTable<E> {

}
