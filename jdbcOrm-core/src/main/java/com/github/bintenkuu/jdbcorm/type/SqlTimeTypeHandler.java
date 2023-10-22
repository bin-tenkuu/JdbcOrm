package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

/**
 * @author bin
 * @since 2023/10/08
 */
public class SqlTimeTypeHandler implements TypeHandler<Time> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Time parameter) throws SQLException {
        ps.setTime(i, parameter);
    }

    @Override
    public Time getResult(java.sql.ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTime(columnIndex);
    }
}
