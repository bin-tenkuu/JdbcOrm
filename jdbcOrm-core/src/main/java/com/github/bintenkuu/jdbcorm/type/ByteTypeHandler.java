package com.github.bintenkuu.jdbcorm.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bin
 * @since 2023/10/08
 */
public class ByteTypeHandler implements TypeHandler<Byte> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Byte parameter) throws SQLException {
        ps.setByte(i, parameter);
    }

    @Override
    public Byte getResult(ResultSet rs, int columnIndex) throws SQLException {
        byte result = rs.getByte(columnIndex);
        return rs.wasNull() ? null : result;
    }
}
