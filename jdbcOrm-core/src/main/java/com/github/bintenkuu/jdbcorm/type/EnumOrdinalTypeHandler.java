package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bin
 * @since 2023/10/08
 */
public class EnumOrdinalTypeHandler<E extends Enum<E>> implements TypeHandler<E> {
    private final Class<E> type;
    private final E[] enums;

    public EnumOrdinalTypeHandler(Class<E> type) {
        this.type = type;
        this.enums = type.getEnumConstants();
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, E parameter) throws SQLException {
        ps.setInt(i, parameter.ordinal());
    }

    @Override
    public E getResult(ResultSet rs, int columnIndex) throws SQLException {
        int ordinal = rs.getInt(columnIndex);
        if (ordinal == 0 && rs.wasNull()) {
            return null;
        }
        return toOrdinalEnum(ordinal);
    }

    private E toOrdinalEnum(int ordinal) {
        try {
            return enums[ordinal];
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Cannot convert " + ordinal + " to " + type.getSimpleName() + " by ordinal value.", ex);
        }
    }
}
