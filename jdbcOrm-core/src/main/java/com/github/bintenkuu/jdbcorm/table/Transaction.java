package com.github.bintenkuu.jdbcorm.table;

import com.github.bintenkuu.jdbcorm.exception.SqlException;
import com.github.bintenkuu.jdbcorm.type.ResultSetHandler;
import com.github.bintenkuu.jdbcorm.type.TypeHandlerRegistry;
import lombok.Getter;
import lombok.val;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.MagicConstant;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author bin
 * @since 2023/10/08
 */
public class Transaction implements Closeable, StringTemplate.Processor<Expression, RuntimeException> {
    private final Connection connection;
    private final TypeHandlerRegistry typeHandlerRegistry;
    @Getter
    private int transactionIsolation;

    private final int originIsolation;
    private final boolean originAutoCommit;

    public static Transaction of(DataSource dataSource, TypeHandlerRegistry typeHandlerRegistry) {
        try {
            return new Transaction(dataSource.getConnection(), typeHandlerRegistry);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public static Transaction of(DataSource dataSource) {
        return of(dataSource, TypeHandlerRegistry.GLOBAL);
    }

    public Transaction(
            Connection connection, TypeHandlerRegistry typeHandlerRegistry
    ) {
        this.connection = connection;
        this.typeHandlerRegistry = typeHandlerRegistry;
        try {
            this.transactionIsolation = originIsolation = connection.getTransactionIsolation();
            originAutoCommit = connection.getAutoCommit();
            if (originAutoCommit) {
                connection.setAutoCommit(false);
            }
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public void setTransactionIsolation(
            @MagicConstant(valuesFromClass = Connection.class) int transactionIsolation
    ) throws SQLException {
        this.transactionIsolation = transactionIsolation;
        connection.setTransactionIsolation(transactionIsolation);
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public void rollback() {
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
            @Language("SQL") String sql,
            ResultSetHandler<E> handler
    ) {
        try (val preparedStatement = connection.prepareStatement(sql)) {
            return executeQuery(preparedStatement, handler);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    private <E> List<E> executeQuery(PreparedStatement ps, ResultSetHandler<E> handler) {
        try (val resultSet = ps.executeQuery()) {
            return handler.getResult(resultSet, typeHandlerRegistry);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public static <E> List<E> executeQuery(
            PreparedStatement ps,
            ResultSetHandler<E> handler,
            TypeHandlerRegistry typeHandlerRegistry
    ) {
        try (val resultSet = ps.executeQuery()) {
            return handler.getResult(resultSet, typeHandlerRegistry);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public int executeUpdate(@Language("SQL") String sql) {
        try (val statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public boolean execute(@Language("SQL") String sql) {
        try (val statement = connection.createStatement()) {
            return statement.execute(sql);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public Expression process(@Language("SQL") String sql) {
        try {
            val ps = connection.prepareStatement(sql);
            return new Expression(ps, typeHandlerRegistry);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    @Override
    public Expression process(StringTemplate st) {
        val sql = String.join("?", st.fragments());
        val expression = process(sql);
        expression.setParameters(st.values());
        return expression;
    }

}
