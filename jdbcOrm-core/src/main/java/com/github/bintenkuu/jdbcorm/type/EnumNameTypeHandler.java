package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bin
 * @since 2023/10/08
 */
public class EnumNameTypeHandler<E extends Enum<E>> implements TypeHandler<E> {
    private final Class<E> type;

    public EnumNameTypeHandler(Class<E> type) {
        if (Enum.class.isAssignableFrom(type)) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("type must be an Enum'");
        }
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, E parameter) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public E getResult(ResultSet rs, int columnIndex) throws SQLException {
        String name = rs.getString(columnIndex);
        return toNameEnum(name);
    }

    private E toNameEnum(String name) {
        return name == null ? null : Enum.valueOf(type, name);
    }
}
