package com.github.bintenkuu.jdbcorm.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bin
 * @since 2023/10/08
 */
public class FloatTypeHandler implements TypeHandler<Float> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Float parameter) throws SQLException {
        ps.setFloat(i, parameter);
    }

    @Override
    public Float getResult(ResultSet rs, int columnIndex) throws SQLException {
        float result = rs.getFloat(columnIndex);
        return rs.wasNull() ? null : result;
    }
}
