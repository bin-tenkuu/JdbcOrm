package com.github.bintenkuu.jdbcorm.type;

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
public interface TypeHandler<T> extends ResultSetHandler<T> {
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
}
