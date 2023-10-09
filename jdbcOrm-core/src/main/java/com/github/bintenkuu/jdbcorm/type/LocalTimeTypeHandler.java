package com.github.bintenkuu.jdbcorm.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

/**
 * @author bin
 * @since 2023/10/08
 */
public class LocalTimeTypeHandler implements TypeHandler<LocalTime> {
    @Override
    public void setParameter(PreparedStatement ps, int i, LocalTime parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalTime getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, LocalTime.class);
    }
}
