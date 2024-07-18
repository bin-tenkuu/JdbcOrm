package com.github.bintenkuu.jdbcorm.interfaces;

import com.github.bintenkuu.jdbcorm.table.TypeHandlerRegistry;
import lombok.val;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 自动选择第一个构造函数
 * 构造函数有参数时，根据类内字段定义的顺序按顺序传入参数
 *
 * @author bin
 * @since 2023/08/31
 */
public final class SampleTable<E> implements ResultSetHandler<E> {
    private final Class<E> clazz;
    private final Constructor<E> constructor;
    private final HashMap<String, SampleColumn<?>> columns;

    @SuppressWarnings("unchecked")
    public SampleTable(
            Class<E> clazz
    ) {
        this.clazz = clazz;
        this.constructor = (Constructor<E>) clazz.getConstructors()[0];
        constructor.setAccessible(true);
        val fields = clazz.getDeclaredFields();
        columns = new HashMap<>(fields.length);
        for (Field declaredField : fields) {
            columns.put(declaredField.getName(), new SampleColumn<>(declaredField));
        }
    }

    public static void main(String[] args) {
        val table = new SampleTable<>(SampleTable.class);
    }

    @Override
    public List<E> getResult(ResultSet resultSet, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
        val metaData = resultSet.getMetaData();
        val columnCount = metaData.getColumnCount();
        val sortColumns = new SampleColumn<?>[columnCount];
        for (int i = 0; i < columnCount; i++) {
            val label = metaData.getColumnLabel(i + 1);
            sortColumns[i] = columns.get(label);
        }
        val list = new ArrayList<E>();
        while (resultSet.next()) {
            // constructor.newInstance();
            // val target = newer.get();
            // for (int i = 0; i < columnCount; i++) {
            //     val result = typeHandlers[i].getResult(resultSet, i + 1);
            //     setters[i].accept(target, result);
            // }
            // list.add(target);
        }
        return list;
    }
}
