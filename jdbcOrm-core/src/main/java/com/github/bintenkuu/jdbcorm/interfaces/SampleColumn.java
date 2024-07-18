package com.github.bintenkuu.jdbcorm.interfaces;

import com.github.bintenkuu.jdbcorm.table.TypeHandlerRegistry;
import lombok.val;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bin
 * @version 1.0.0
 * @since 2024/07/18
 */
public final class SampleColumn<T> implements ResultSetHandler<T> {
    private final Field field;
    private final Class<T> type;
    private final String name;

    public SampleColumn(Field field) {
        this.field = field;
        field.setAccessible(true);
        this.type = (Class<T>) field.getType();
        this.name = field.getName();
    }

    @Override
    public List<T> getResult(ResultSet resultSet, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
        val metaData = resultSet.getMetaData();
        TypeHandler<T> typeHandler = typeHandlerRegistry.getTypeHandler(type);
        int index = 0;
        for (int i = 1, length = metaData.getColumnCount(); i <= length; i++) {
            val label = metaData.getColumnLabel(i);
            if (name.equals(label)) {
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
