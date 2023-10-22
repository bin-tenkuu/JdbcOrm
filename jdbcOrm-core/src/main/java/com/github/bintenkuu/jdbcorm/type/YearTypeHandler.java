package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;

/**
 * @author bin
 * @since 2023/10/08
 */
public class YearTypeHandler implements TypeHandler<Year> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Year parameter) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public Year getResult(ResultSet rs, int columnIndex) throws SQLException {
        int year = rs.getInt(columnIndex);
        return year == 0 && rs.wasNull() ? null : Year.of(year);
    }
}
