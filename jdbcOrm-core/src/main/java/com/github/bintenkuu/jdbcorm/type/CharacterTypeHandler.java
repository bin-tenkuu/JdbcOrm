package com.github.bintenkuu.jdbcorm.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bin
 * @since 2023/10/08
 */
public class CharacterTypeHandler implements TypeHandler<Character> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Character parameter) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public Character getResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        if (columnValue != null && !columnValue.isEmpty()) {
            return columnValue.charAt(0);
        }
        return null;
    }
}
