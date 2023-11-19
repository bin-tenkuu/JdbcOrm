package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author bin
 * @since 2023/10/08
 */
public class LocalDateTimeTypeHandler implements TypeHandler<LocalDateTime> {
    public static final LocalDateTimeTypeHandler INSTANCE = new LocalDateTimeTypeHandler();

    @Override
    public void setParameter(PreparedStatement ps, int i, LocalDateTime parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalDateTime getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, LocalDateTime.class);
    }
}
