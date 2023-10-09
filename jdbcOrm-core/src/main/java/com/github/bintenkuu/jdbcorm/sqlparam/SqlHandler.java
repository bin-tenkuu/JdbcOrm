package com.github.bintenkuu.jdbcorm.sqlparam;

import lombok.AllArgsConstructor;

/**
 * @author bin
 * @since 2023/10/14
 */
public interface SqlHandler {
    void handleSql(StringBuilder sql);

    static SqlHandler raw(String sql) {
        return new Raw(sql);
    }

    static SqlHandler raw(Iterable<String> sql) {
        return new Raw(String.join(",", sql));
    }

    @AllArgsConstructor
    class Raw implements SqlHandler {
        private final String sql;

        @Override
        public void handleSql(StringBuilder sql) {
            sql.append(this.sql);
        }
    }
}
