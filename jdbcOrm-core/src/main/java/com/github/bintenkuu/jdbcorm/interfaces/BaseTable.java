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
        val columns = columns();
        val typeHandlers = new TypeHandler[columnCount];
        val setters = new BiConsumer[columnCount];
        for (int i = 0, length = setters.length; i < length; i++) {
            val label = metaData.getColumnLabel(i + 1);
            val column = columns.get(label);
            typeHandlers[i] = typeHandlerRegistry.getTypeHandler(column.typeClass());
            setters[i] = column.setter();
        }
        val newer = newer();
        val list = new ArrayList<E>();
        while (resultSet.next()) {
            val target = newer.get();
            for (int i = 0; i < columnCount; i++) {
                val result = typeHandlers[i].getResult(resultSet, i + 1);
                setters[i].accept(target, result);
            }
            list.add(target);
        }
        return list;
    }
}
