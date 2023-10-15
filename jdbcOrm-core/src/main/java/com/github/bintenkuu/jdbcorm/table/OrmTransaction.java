package com.github.bintenkuu.jdbcorm.table;

import com.github.bintenkuu.jdbcorm.type.ResultSetHandler;
import com.github.bintenkuu.jdbcorm.type.TypeHandlerRegistry;
import lombok.Getter;
import lombok.val;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author bin
 * @since 2023/10/08
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "SqlSourceToSinkFlow"})
public class OrmTransaction implements Closeable {
    @NotNull
    @Getter
    private final TypeHandlerRegistry typeHandlerRegistry;
    @NotNull
    @Getter
    private final Connection connection;
    @Getter
    private int transactionIsolation;

    private final int originIsolation;
    private final boolean originAutoCommit;

    public static OrmTransaction of(
            DataSource dataSource
    ) {
        try {
            return new OrmTransaction(
                    dataSource.getConnection(),
                    TypeHandlerRegistry.GLOBAL,
                    false
            );
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public OrmTransaction(
            @NotNull Connection connection,
            @NotNull TypeHandlerRegistry typeHandlerRegistry,
            boolean autoCommit
    ) throws SQLException {
        Objects.requireNonNull(connection, "'dataSource' must not be null");
        Objects.requireNonNull(typeHandlerRegistry, "'typeHandlerRegistry' must not be null");
        this.connection = connection;
        this.typeHandlerRegistry = typeHandlerRegistry;
        this.transactionIsolation = originIsolation = connection.getTransactionIsolation();
        if (autoCommit) {
            originAutoCommit = false;
        } else {
            originAutoCommit = connection.getAutoCommit();
            if (originAutoCommit) {
                connection.setAutoCommit(false);
            }
        }
    }

    public void setTransactionIsolation(
            @MagicConstant(valuesFromClass = Connection.class) int transactionIsolation
    ) throws SqlException {
        this.transactionIsolation = transactionIsolation;
        try {
            connection.setTransactionIsolation(transactionIsolation);
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public void commit() throws SqlException {
        try {
            connection.commit();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public void rollback() throws SqlException {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    @Override
    public void close() {
        try {
            if (transactionIsolation != originIsolation) {
                connection.setTransactionIsolation(originIsolation);
            }
            if (originAutoCommit) {
                connection.setAutoCommit(true);
            }
        } catch (SQLException ignored) {
        } finally {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public <E> List<E> executeQuery(
            @Language("SQL") String sql, ResultSetHandler<E> handler
    ) throws SqlException {
        try (val preparedStatement = connection.prepareStatement(sql)) {
            return executeQuery(preparedStatement, handler, typeHandlerRegistry);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public static <E> List<E> executeQuery(
            PreparedStatement ps,
            ResultSetHandler<E> handler,
            TypeHandlerRegistry typeHandlerRegistry
    ) throws SqlException {
        try (val resultSet = ps.executeQuery()) {
            return handler.getResult(resultSet, typeHandlerRegistry);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public int executeUpdate(@Language("SQL") String sql) throws SqlException {
        try (val statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public boolean execute(@Language("SQL") String sql) throws SqlException {
        try (val statement = connection.createStatement()) {
            return statement.execute(sql);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public Expression process(@Language("SQL") String sql) throws SqlException {
        try {
            return new Expression(typeHandlerRegistry, connection.prepareStatement(sql));
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

}
