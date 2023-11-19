package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;

/**
 * @author bin
 * @since 2023/10/08
 */
public class YearMonthTypeHandler implements TypeHandler<YearMonth> {
    public static final YearMonthTypeHandler INSTANCE = new YearMonthTypeHandler();

    @Override
    public void setParameter(PreparedStatement ps, int i, YearMonth parameter) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public YearMonth getResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : YearMonth.parse(value);
    }
}
