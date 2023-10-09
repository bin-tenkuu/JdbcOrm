package com.github.bintenkuu.jdbcorm.table;

import com.github.bintenkuu.jdbcorm.sqlparam.ParameterHandler;
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
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

@Getter
@AllArgsConstructor
public class Column<E, T> implements ResultSetHandler<T>, ParameterHandler<T> {
    private final String name;
    private final Class<T> typeClass;
    private final BiConsumer<E, T> setter;

    public static <E, T> Column<E, T> of(
            String name, Class<T> clazz,
            BiConsumer<E, T> setter
    ) {
        return new Column<>(name, clazz, setter);
    }

    public static <E, T> Column<E, T> id(Class<T> clazz, BiConsumer<E, T> setter) {
        return of("id", clazz, setter);
    }

    public static <E> Column<E, Long> id(BiConsumer<E, Long> setter) {
        return of("id", Long.class, setter);
    }

    public static <E> Column<E, String> name(BiConsumer<E, String> setter) {
        return of("name", String.class, setter);
    }

    @Override
    public List<T> getResult(ResultSet resultSet, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
        val metaData = resultSet.getMetaData();
        TypeHandler<T> typeHandler = typeHandlerRegistry.getTypeHandler(typeClass);
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

    public SqlHandler raw() {
        return SqlHandler.raw(name);
    }

    @Override
    public void handleSql(StringBuilder sql) {
        sql.append("?");
    }

    @Override
    public void handleTypeHandlerList(TypeHandlerRegistry typeHandlerRegistry, List<TypeHandler<?>> typeHandlerList) {
        typeHandlerList.add(typeHandlerRegistry.getTypeHandler(typeClass));
    }

    @Override
    public List<T> handleParam(T obj) {
        return Collections.singletonList(obj);
    }
}
