package com.github.bintenkuu.jdbcorm.interfaces;

import com.github.bintenkuu.jdbcorm.table.TypeHandlerRegistry;
import lombok.val;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author bin
 * @since 2023/10/08
 */
public interface TypeHandler<T> extends ResultSetHandler<T> {
    void setParameter(PreparedStatement ps, int i, T parameter) throws SQLException;

    T getResult(ResultSet rs, int columnIndex) throws SQLException;

    default List<T> getResult(ResultSet rs, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
        if (rs.getMetaData().getColumnCount() < 1) {
            return List.of();
        }
        val list = new ArrayList<T>();
        while (rs.next()) {
            list.add(getResult(rs, 1));
        }
        return list;
    }

    default <R> TypeHandler<R> wrapper(Function<? super T, ? extends R> unwrapped, Function<? super R, ? extends T> wrapper) {
        return new TypeHandlerWrapper<>(this, wrapper, unwrapped);
    }

    record TypeHandlerWrapper<Raw, T>(
            TypeHandler<Raw> type,
            Function<? super T, ? extends Raw> unwrapped,
            Function<? super Raw, ? extends T> wrapper
    ) implements TypeHandler<T> {

        @Override
        public void setParameter(PreparedStatement ps, int i, T parameter) throws SQLException {
            val raw = unwrapped.apply(parameter);
            type.setParameter(ps, i, raw);
        }

        @Override
        public T getResult(ResultSet rs, int columnIndex) throws SQLException {
            val raw = type.getResult(rs, columnIndex);
            if (raw == null) {
                return null;
            }
            return wrapper.apply(raw);
        }
    }
}
