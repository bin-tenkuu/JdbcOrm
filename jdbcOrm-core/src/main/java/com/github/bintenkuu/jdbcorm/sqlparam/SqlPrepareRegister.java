package com.github.bintenkuu.jdbcorm.sqlparam;

import com.github.bintenkuu.jdbcorm.type.TypeHandler;
import com.github.bintenkuu.jdbcorm.type.TypeHandlerRegistry;
import lombok.AllArgsConstructor;
import lombok.val;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author bin
 * @since 2023/10/13
 */
public class SqlPrepareRegister {
    public static final SqlPrepareRegister GLOBAL = new SqlPrepareRegister();
    private final HashMap<Class<?>, ParameterHandler<?>> allSqlPrepareMap = new HashMap<>();

    public <T> void register(Class<T> clazz, ParameterHandler<T> typeHandler) {
        allSqlPrepareMap.put(clazz, typeHandler);
    }

    @SuppressWarnings("unchecked")
    public <T> ParameterHandler<T> getSqlPrepare(Class<T> clazz) {
        val prepare = (ParameterHandler<T>) allSqlPrepareMap.get(clazz);
        return prepare != null ? prepare : new Default<>(clazz);
    }

    @AllArgsConstructor
    private static final class Default<T> implements ParameterHandler<T> {
        private final Class<T> clazz;

        @Override
        public void handleSql(StringBuilder sql) {
            sql.append("?");
        }

        @Override
        public void handleTypeHandlerList(TypeHandlerRegistry typeHandlerRegistry,
                List<TypeHandler<?>> typeHandlerList) {
            typeHandlerList.add(typeHandlerRegistry.getTypeHandler(clazz));
        }

        @Override
        public List<T> handleParam(T obj) {
            return Collections.singletonList(obj);
        }
    }
}
