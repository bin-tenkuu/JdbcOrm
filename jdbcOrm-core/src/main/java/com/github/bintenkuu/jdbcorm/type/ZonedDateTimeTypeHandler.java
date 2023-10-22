package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

/**
 * @author bin
 * @since 2023/10/08
 */
public class ZonedDateTimeTypeHandler implements TypeHandler<ZonedDateTime> {
    @Override
    public void setParameter(PreparedStatement ps, int i, ZonedDateTime parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public ZonedDateTime getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, ZonedDateTime.class);
    }
}
