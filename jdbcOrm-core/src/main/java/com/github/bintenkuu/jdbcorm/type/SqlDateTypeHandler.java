package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bin
 * @since 2023/10/08
 */
public class SqlDateTypeHandler implements TypeHandler<Date> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Date parameter) throws SQLException {
        ps.setDate(i, parameter);
    }

    @Override
    public Date getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }
}
