package com.github.bintenkuu.jdbcorm.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

/**
 * @author bin
 * @since 2023/10/08
 */
public class OffsetDateTimeTypeHandler implements TypeHandler<OffsetDateTime> {
    @Override
    public void setParameter(PreparedStatement ps, int i, OffsetDateTime parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public OffsetDateTime getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, OffsetDateTime.class);
    }
}
