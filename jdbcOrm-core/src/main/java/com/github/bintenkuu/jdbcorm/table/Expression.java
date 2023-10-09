package com.github.bintenkuu.jdbcorm.table;

import com.github.bintenkuu.jdbcorm.exception.SqlException;
import com.github.bintenkuu.jdbcorm.sqlparam.ParameterHandler;
import com.github.bintenkuu.jdbcorm.type.ResultSetHandler;
import com.github.bintenkuu.jdbcorm.type.TypeHandler;
import com.github.bintenkuu.jdbcorm.type.TypeHandlerRegistry;
import lombok.val;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @author bin
 * @since 2023/10/09
 */
public class Expression {
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final PreparedStatement preparedStatement;
    private final List<ParameterHandler<?>> sqlPrepareList;
    private final List<TypeHandler<?>> typeHandlerList;

    public Expression(
            TypeHandlerRegistry typeHandlerRegistry,
            PreparedStatement preparedStatement,
            List<ParameterHandler<?>> sqlPrepareList,
            List<TypeHandler<?>> typeHandlerList
    ) {
        this.typeHandlerRegistry = typeHandlerRegistry;
        this.preparedStatement = preparedStatement;
        this.sqlPrepareList = sqlPrepareList;
        this.typeHandlerList = typeHandlerList;
    }

    public Expression(TypeHandlerRegistry typeHandlerRegistry, PreparedStatement preparedStatement) {
        this(typeHandlerRegistry, preparedStatement, Collections.emptyList(), Collections.emptyList());
    }

    public <E> List<E> executeQuery(ResultSetHandler<E> handler) throws SqlException {
        try (preparedStatement) {
            return executeQueryBatch(handler);
        } catch (SQLException ex) {
            throw new SqlException(ex);
        }
    }

    public <E> List<E> executeQueryBatch(ResultSetHandler<E> handler) throws SqlException {
        return OrmTransaction.executeQuery(preparedStatement, handler, typeHandlerRegistry);
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

    public void setParams(Object... parameters) {
        setParameters(parameters);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <E> void setParameters(E[] parameters) throws SqlException {
        try {
            int objIndex = 0;
            int index = 1;
            val typeHandlerIter = typeHandlerList.iterator();
            for (ParameterHandler prepare : sqlPrepareList) {
                val objects = prepare.handleParam(parameters[objIndex++]);
                for (Object object : objects) {
                    ((TypeHandler<Object>) typeHandlerIter.next()).setParameter(preparedStatement, index++, object);
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
