package com.github.bintenkuu.jdbcorm.interfaces;

import com.github.bintenkuu.jdbcorm.table.TypeHandlerRegistry;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author bin
 * @version 1.0.0
 * @since 2023/10/22
 */
public interface BaseColumn<E, T> extends ResultSetHandler<T> {
    String name();

    Class<T> typeClass();

    BiConsumer<E, T> setter();

    @Override
    default List<T> getResult(ResultSet resultSet, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
        val metaData = resultSet.getMetaData();
        TypeHandler<T> typeHandler = typeHandlerRegistry.getTypeHandler(typeClass());
        int index = 0;
        for (int i = 1, length = metaData.getColumnCount(); i <= length; i++) {
            val label = metaData.getColumnLabel(i);
            if (name().equals(label)) {
                index = i;
                break;
            }
        }
        val list = new ArrayList<T>();
        if (index != 0) {
            while (resultSet.next()) {
                val result = typeHandler.getResult(resultSet, index);
                list.add(result);
            }
        }
        return list;
    }

}
