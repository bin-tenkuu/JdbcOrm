package com.github.bintenkuu.jdbcorm.interfaces.impl;

import com.github.bintenkuu.jdbcorm.interfaces.BaseColumn;
import com.github.bintenkuu.jdbcorm.interfaces.FieldMap;
import com.github.bintenkuu.jdbcorm.interfaces.ResultSetHandler;
import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;
import com.github.bintenkuu.jdbcorm.table.TypeHandlerRegistry;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ColumnMapTypeHandler(
        Map<String, ? extends BaseColumn<?, ?>> columns
) implements ResultSetHandler<FieldMap> {
    @SuppressWarnings("unchecked")
    @Override
    public List<FieldMap> getResult(ResultSet resultSet, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
        val columns = columns();

        val metaData = resultSet.getMetaData();
        val columnCount = metaData.getColumnCount();
        val typeHandlers = new TypeHandler[columnCount];
        val setters = new BaseColumn[columnCount];
        for (int i = 0; i < columnCount; i++) {
            val label = metaData.getColumnLabel(i + 1);
            val column = columns.get(label);
            if (column != null) {
                typeHandlers[i] = typeHandlerRegistry.getTypeHandler(column.typeClass());
                setters[i] = column;
            }
        }
        val list = new ArrayList<FieldMap>();
        while (resultSet.next()) {
            val target = new FieldMap(columnCount);
            for (int i = 0; i < columnCount; i++) {
                val result = typeHandlers[i].getResult(resultSet, i + 1);
                target.put(setters[i], result);
            }
            list.add(target);
        }
        return list;
    }
}
