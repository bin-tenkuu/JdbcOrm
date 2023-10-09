package com.github.bintenkuu.jdbcorm.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bin
 * @since 2023/10/08
 */
public class BooleanTypeHandler implements TypeHandler<Boolean> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Boolean parameter) throws SQLException {
        ps.setBoolean(i, parameter);
    }

    @Override
    public Boolean getResult(ResultSet rs, int columnIndex) throws SQLException {
        boolean result = rs.getBoolean(columnIndex);
        return rs.wasNull() ? null : result;
    }
}
