package com.github.bintenkuu.jdbcorm.sqlparam;

import com.github.bintenkuu.jdbcorm.type.TypeHandler;
import com.github.bintenkuu.jdbcorm.type.TypeHandlerRegistry;

import java.util.List;

/**
 * @author bin
 * @since 2023/10/13
 */
public interface ParameterHandler<T> extends SqlHandler {

    void handleSql(StringBuilder sql);

    void handleTypeHandlerList(TypeHandlerRegistry typeHandlerRegistry, List<TypeHandler<?>> typeHandlerList);

    List<?> handleParam(T obj);
}
