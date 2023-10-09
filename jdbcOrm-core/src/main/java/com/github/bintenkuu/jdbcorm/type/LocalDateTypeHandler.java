package com.github.bintenkuu.jdbcorm.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * @author bin
 * @since 2023/10/08
 */
public class LocalDateTypeHandler implements TypeHandler<LocalDate> {
    @Override
    public void setParameter(PreparedStatement ps, int i, LocalDate parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalDate getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, LocalDate.class);
    }
}
