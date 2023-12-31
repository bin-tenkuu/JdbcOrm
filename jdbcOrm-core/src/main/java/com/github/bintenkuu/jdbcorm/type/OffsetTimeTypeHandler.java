package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetTime;

/**
 * @author bin
 * @since 2023/10/08
 */
public class OffsetTimeTypeHandler implements TypeHandler<OffsetTime> {
    public static final OffsetTimeTypeHandler INSTANCE = new OffsetTimeTypeHandler();

    @Override
    public void setParameter(PreparedStatement ps, int i, OffsetTime parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public OffsetTime getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, OffsetTime.class);
    }
}
