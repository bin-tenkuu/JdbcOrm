package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.sqlparam.ParameterHandler;
import lombok.val;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bin
 * @since 2023/10/08
 */
public interface TypeHandler<T> extends ResultSetHandler<T>, ParameterHandler<T> {
    void setParameter(PreparedStatement ps, int i, T parameter) throws SQLException;

    T getResult(ResultSet rs, int columnIndex) throws SQLException;

    default List<T> getResult(ResultSet rs, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
        if (rs.getMetaData().getColumnCount() < 1) {
            return List.of();
        }
        val list = new ArrayList<T>();
        while (rs.next()) {
            list.add(getResult(rs, 1));
        }
        return list;
    }

    default void handleSql(StringBuilder sql) {
        sql.append("?");
    }

    @Override
    default void handleTypeHandlerList(TypeHandlerRegistry typeHandlerRegistry, List<TypeHandler<?>> typeHandlerList) {
        typeHandlerList.add(this);
    }

    default List<?> handleParam(T obj) {
        return List.of(obj);
    }

}
