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
 * @version 1.0.0
 * @since 2023/10/22
 */
public interface BaseTable<E> extends ResultSetHandler<E> {
    Supplier<E> newer();

    Map<String, ? extends BaseColumn<? super E, ?>> columns();

    @SuppressWarnings("unchecked")
    @Override
    default List<E> getResult(ResultSet resultSet, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
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
