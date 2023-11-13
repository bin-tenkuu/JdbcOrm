package com.github.bintenkuu.jdbcorm.util;

import com.github.bintenkuu.jdbcorm.interfaces.BaseColumn;
import com.github.bintenkuu.jdbcorm.interfaces.BaseTable;
import com.github.bintenkuu.jdbcorm.interfaces.impl.BaseColumnImpl;
import com.github.bintenkuu.jdbcorm.interfaces.impl.BaseTableImpl;
import lombok.val;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author bin
 * @version 1.0.0
 * @since 2023/10/22
 */
public class TableUtil {
    public static <E, T> BaseColumn<E, T> column(
            String name, Class<T> typeClass,
            BiConsumer<E, T> setter
    ) {
        return new BaseColumnImpl<>(name, typeClass, setter);
    }

    public static <E, T> BaseColumn<E, T> columnId(Class<T> typeClass, BiConsumer<E, T> setter) {
        return column("id", typeClass, setter);
    }

    public static <E> BaseColumn<E, Long> columnId(BiConsumer<E, Long> setter) {
        return column("id", Long.class, setter);
    }

    public static <E> BaseColumn<E, String> columnName(BiConsumer<E, String> setter) {
        return column("name", String.class, setter);
    }

    public static <E> BaseTable<E> of(
            Supplier<E> newer,
            Map<String, ? extends BaseColumn<? super E, ?>> map
    ) {
        return new BaseTableImpl<>(newer, map);
    }

    public static <E> BaseTable<E> of(
            Supplier<E> newer,
            Collection<? extends BaseColumn<? super E, ?>> columns
    ) {
        val map = new LinkedHashMap<String, BaseColumn<? super E, ?>>(columns.size());
        for (val column : columns) {
            map.put(column.name(), column);
        }
        return of(newer, map);
    }

    @SafeVarargs
    public static <E> BaseTable<E> of(Supplier<E> newer, BaseColumn<? super E, ?>... columns) {
        return of(newer, List.of(columns));
    }

}
