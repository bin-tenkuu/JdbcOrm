package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author bin
 * @since 2023/10/08
 */
public class SqlTimestampTypeHandler implements TypeHandler<Timestamp> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Timestamp parameter) throws SQLException {
        ps.setTimestamp(i, parameter);
    }

    @Override
    public Timestamp getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTimestamp(columnIndex);
    }
}
