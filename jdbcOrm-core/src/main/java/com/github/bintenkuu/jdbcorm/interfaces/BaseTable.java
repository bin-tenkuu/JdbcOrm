package com.github.bintenkuu.jdbcorm.interfaces;

import com.github.bintenkuu.jdbcorm.table.TypeHandlerRegistry;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author bin
 * @since 2023/08/31
 */
public record BaseTable<E>(
        Supplier<E> newer,
        Map<String, ? extends BaseColumn<? super E, ?>> columns
) implements ResultSetHandler<E> {

    @SuppressWarnings("unchecked")
    @Override
    public List<E> getResult(ResultSet resultSet, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
        val metaData = resultSet.getMetaData();
        val columnCount = metaData.getColumnCount();
        val sortColumns = (BaseColumn<? super E, ?>[]) new BaseColumn<?, ?>[columnCount];
        for (int i = 0; i < columnCount; i++) {
            val label = metaData.getColumnLabel(i + 1);
            sortColumns[i] = columns.get(label);
        }
        val list = new ArrayList<E>();
        while (resultSet.next()) {
            val target = newer.get();
            for (int i = 0; i < columnCount; i++) {
                val sortColumn = sortColumns[i];
                val result = typeHandlerRegistry.getTypeHandler(sortColumn.typeClass()).getResult(resultSet, i + 1);
                ((BiConsumer<E, Object>) sortColumn.setter()).accept(target, result);
            }
            list.add(target);
        }
        return list;
    }
}
