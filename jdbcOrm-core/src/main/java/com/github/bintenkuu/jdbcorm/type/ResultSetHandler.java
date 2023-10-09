package com.github.bintenkuu.jdbcorm.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author bin
 * @since 2023/10/08
 */
public interface ResultSetHandler<T> {

    List<T> getResult(ResultSet resultSet, TypeHandlerRegistry typeHandlerRegistry) throws SQLException;
}
