package com.github.bintenkuu.jdbcorm.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bin
 * @since 2023/10/08
 */
public class ByteArrayTypeHandler implements TypeHandler<byte[]> {

    @Override
    public void setParameter(PreparedStatement ps, int i, byte[] parameter) throws SQLException {
        ps.setBytes(i, parameter);
    }

    @Override
    public byte[] getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBytes(columnIndex);
    }
}