package com.github.bintenkuu.jdbcorm.table;

import com.github.bintenkuu.jdbcorm.sqlparam.SqlHandler;
import com.github.bintenkuu.jdbcorm.type.ResultSetHandler;
import com.github.bintenkuu.jdbcorm.type.TypeHandler;
import com.github.bintenkuu.jdbcorm.type.TypeHandlerRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author bin
 * @since 2023/08/31
 */
@AllArgsConstructor
@Getter
public class Table<E> implements ResultSetHandler<E> {
    private final Supplier<E> newer;
    private final Map<String, Column<? super E, ?>> columns;

    @SafeVarargs
    public static <E> Table<E> of(Supplier<E> newer, Column<? super E, ?>... columns) {
        val map = new LinkedHashMap<String, Column<? super E, ?>>(columns.length);
        for (val column : columns) {
            map.put(column.getName(), column);
        }
        return new Table<>(newer, map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> getResult(ResultSet resultSet, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
        val metaData = resultSet.getMetaData();
        val setters = new MetaSetter[metaData.getColumnCount()];
        for (int i = 0, length = setters.length; i < length; i++) {
            val label = metaData.getColumnLabel(i + 1);
            val column = columns.get(label);
            setters[i] = ofMetaSetter(i + 1, column, typeHandlerRegistry);
        }
        val list = new ArrayList<E>();
        while (resultSet.next()) {
            val target = newer.get();
            for (val setter : setters) {
                setter.setField(resultSet, target);
            }
            list.add(target);
        }
        return list;
    }

    public SqlHandler all() {
        return SqlHandler.raw(columns.keySet());
    }

    @AllArgsConstructor
    private static class MetaSetter<E, T> {
        private final int index;
        private final TypeHandler<T> typeHandler;
        private final BiConsumer<E, T> setter;

        private void setField(ResultSet resultSet, E target) throws SQLException {
            val result = typeHandler.getResult(resultSet, index);
            setter.accept(target, result);
        }

    }

    private static <E, T> MetaSetter<E, ?> ofMetaSetter(
            int index, Column<E, T> column, TypeHandlerRegistry typeHandlerRegistry
    ) {
        return new MetaSetter<>(
                index,
                typeHandlerRegistry.getTypeHandler(column.getTypeClass()),
                column.getSetter()
        );
    }
}
