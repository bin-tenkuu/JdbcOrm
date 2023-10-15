package com.github.bintenkuu.jdbcorm.table;

import com.github.bintenkuu.jdbcorm.type.ResultSetHandler;
import com.github.bintenkuu.jdbcorm.type.TypeHandlerRegistry;
import lombok.val;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * @author bin
 * @since 2023/10/09
 */
public class Expression {
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final PreparedStatement preparedStatement;
    private volatile int parameterCount = -1;

    public Expression(
            TypeHandlerRegistry typeHandlerRegistry,
            PreparedStatement preparedStatement
    ) {
        this.typeHandlerRegistry = typeHandlerRegistry;
        this.preparedStatement = preparedStatement;
    }

    public <E> List<E> executeQuery(ResultSetHandler<E> handler) throws SqlException {
        try (preparedStatement) {
            try (val resultSet = preparedStatement.executeQuery()) {
                return handler.getResult(resultSet, typeHandlerRegistry);
            }
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public int executeUpdate() throws SqlException {
        try (preparedStatement) {
            return executeUpdateBatch();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public int executeUpdateBatch() throws SqlException {
        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public boolean execute() throws SqlException {
        try (preparedStatement) {
            return executeBatch();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public boolean executeBatch() throws SqlException {
        try {
            return preparedStatement.execute();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public void clearBatch() throws SqlException {
        try {
            preparedStatement.clearBatch();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public void addBatch() throws SqlException {
        try {
            preparedStatement.addBatch();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    private void initCache() throws SqlException {
        if (parameterCount >= 0) {
            return;
        }
        try {
            synchronized (this) {
                if (parameterCount >= 0) {
                    return;
                }
                parameterCount = preparedStatement.getParameterMetaData().getParameterCount();
            }
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public void setParams(Object... parameters) {
        setParameters(parameters);
    }

    public void setParameters(Object[] parameters) throws SqlException {
        initCache();
        try {
            if (this.parameterCount != parameters.length) {
                val string = STR. "paramLength must be \{ parameterCount }, current : \{ parameters.length }" ;
                throw new IllegalArgumentException(string);
            }
            val ps = preparedStatement;
            for (int i = 0; i < parameters.length; ) {
                val parameter = parameters[i];
                i++;
                if (parameter == null) {
                    ps.setNull(i, Types.VARCHAR);
                } else {
                    val handler = typeHandlerRegistry.getTypeHandler((Type) parameter.getClass());
                    handler.setParameter(ps, i, parameter);
                }
            }
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public void setParameters(List<?> parameters) throws SqlException {
        setParameters(parameters.toArray());
    }
}
