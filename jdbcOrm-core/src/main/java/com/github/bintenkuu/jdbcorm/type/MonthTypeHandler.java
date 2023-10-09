package com.github.bintenkuu.jdbcorm.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;

/**
 * @author bin
 * @since 2023/10/08
 */
public class MonthTypeHandler implements TypeHandler<Month> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Month parameter) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public Month getResult(ResultSet rs, int columnIndex) throws SQLException {
        int month = rs.getInt(columnIndex);
        return month == 0 && rs.wasNull() ? null : Month.of(month);
    }
}
