package com.github.bintenkuu.jdbcorm.exception;

/**
 * @author bin
 * @since 2023/10/09
 */
public class SqlException extends RuntimeException {
    public SqlException(String message) {
        super(message);
    }

    public SqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlException(Throwable cause) {
        super(cause);
    }
}
