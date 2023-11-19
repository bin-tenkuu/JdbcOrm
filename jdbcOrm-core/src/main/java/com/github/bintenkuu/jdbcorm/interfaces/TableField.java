package com.github.bintenkuu.jdbcorm.interfaces;

import com.github.bintenkuu.jdbcorm.table.TypeHandlerRegistry;
import com.github.bintenkuu.jdbcorm.type.ObjectTypeHandler;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bin
 * @version 1.0.0
 * @since 2023/11/19
 */
@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class TableField<T> implements TypeHandler<T>, ResultSetHandler<T> {
    private static final int LEVEL_TYPE = 1;

    private final int level;
    @NotNull
    private final String name;
    @NotNull
    private final TypeHandler<T> typeHandler;

    public static <T> TableField<T> of(String name, TypeHandler<T> typeHandler) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (typeHandler == null) {
            throw new IllegalArgumentException("typeHandler is null");
        }
        return new TableField<>(LEVEL_TYPE, name, typeHandler);
    }

    public static TableField<Object> of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        return new TableField<>(LEVEL_TYPE, name, ObjectTypeHandler.INSTANCE);
    }

    public <T2> TableField<T2> withType(TypeHandler<T2> typeHandler) {
        if (typeHandler == null) {
            throw new IllegalArgumentException("typeHandler is null");
        }
        return new TableField<>(LEVEL_TYPE, name, typeHandler);
    }

    public TableField<T> withName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        return new TableField<>(level, name, typeHandler);
    }

    public String name() {
        return name;
    }

    public TypeHandler<T> typeHandler() {
        return typeHandler;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter) throws SQLException {
        typeHandler.setParameter(ps, i, parameter);
    }

    @Override
    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        return typeHandler.getResult(rs, columnIndex);
    }

    @Override
    public List<T> getResult(ResultSet resultSet, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
        val metaData = resultSet.getMetaData();
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
