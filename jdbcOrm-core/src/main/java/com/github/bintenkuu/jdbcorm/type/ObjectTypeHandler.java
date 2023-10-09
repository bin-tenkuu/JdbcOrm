package com.github.bintenkuu.jdbcorm.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bin
 * @since 2023/10/09
 */
public class ObjectTypeHandler implements TypeHandler<Object> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Object parameter) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex);
    }
}
