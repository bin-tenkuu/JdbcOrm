package com.github.bintenkuu.jdbcorm.table;

import com.github.bintenkuu.jdbcorm.exception.SqlException;
import com.github.bintenkuu.jdbcorm.type.ResultSetHandler;
import com.github.bintenkuu.jdbcorm.type.TypeHandlerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * @author bin
 * @since 2023/10/09
 */
@RequiredArgsConstructor
public class Expression {
    private final PreparedStatement preparedStatement;
    private final TypeHandlerRegistry typeHandlerRegistry;
    private int parameterCount = -1;

    public <E> List<E> executeQuery(ResultSetHandler<E> handler) {
        try (preparedStatement) {
            return executeQueryBatch(handler);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public <E> List<E> executeQueryBatch(ResultSetHandler<E> handler) {
        return Transaction.executeQuery(preparedStatement, handler, typeHandlerRegistry);
    }

    public int executeUpdate() {
        try (preparedStatement) {
            return executeUpdateBatch();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public int executeUpdateBatch() {
        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public boolean execute() {
        try (preparedStatement) {
            return executeBatch();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public boolean executeBatch() {
        try {
            return preparedStatement.execute();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    private void initCached() {
        try {
            if (parameterCount < 0) {
                val metaData = preparedStatement.getParameterMetaData();
                parameterCount = metaData.getParameterCount();
            }
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public void clearBatch() {
        try {
            preparedStatement.clearBatch();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public void addBatch() {
        try {
            preparedStatement.addBatch();
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public void setParams(Object... parameters) {
        setParameters(parameters);
    }

    public <E> void setParameters(E[] parameters) {
        initCached();
        if (parameters == null || parameterCount != parameters.length) {
            throw new SqlException("parameters size not match");
        }
        try {
            val ps = preparedStatement;
            for (int index = 1, length = parameters.length; index <= length; index++) {
                Object value = parameters[index - 1];
                if (value == null) {
                    ps.setNull(index, Types.VARCHAR);
                } else {
                    val typeHandler = typeHandlerRegistry.getTypeHandler(value.getClass(), 0);
                    if (typeHandler != null) {
                        typeHandler.setParameter(ps, index, value);
                    } else {
                        ps.setObject(index, value);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public void setParameters(List<?> parameters) {
        setParameters(parameters.toArray());
    }
}
