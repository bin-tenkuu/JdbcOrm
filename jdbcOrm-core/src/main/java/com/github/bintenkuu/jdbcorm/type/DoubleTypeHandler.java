package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bin
 * @since 2023/10/08
 */
public class DoubleTypeHandler implements TypeHandler<Double> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Double parameter) throws SQLException {
        ps.setDouble(i, parameter);
    }

    @Override
    public Double getResult(ResultSet rs, int columnIndex) throws SQLException {
        double result = rs.getDouble(columnIndex);
        return rs.wasNull() ? null : result;
    }
}
