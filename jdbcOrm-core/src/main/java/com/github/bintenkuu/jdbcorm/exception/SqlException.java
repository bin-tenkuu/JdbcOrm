package com.github.bintenkuu.jdbcorm.exception;

import java.sql.SQLException;

/**
 * @author bin
 * @since 2023/10/09
 */
public class SqlException extends RuntimeException {
    public SqlException(String message) {
        super(message);
    }

    public SqlException(SQLException cause) {
        super(cause);
    }

    @Override
    public SQLException getCause() {
        return (SQLException) super.getCause();
    }
}
