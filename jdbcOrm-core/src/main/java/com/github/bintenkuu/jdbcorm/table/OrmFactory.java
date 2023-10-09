package com.github.bintenkuu.jdbcorm.table;

import com.github.bintenkuu.jdbcorm.exception.SqlException;
import com.github.bintenkuu.jdbcorm.sqlparam.SqlPrepareRegister;
import com.github.bintenkuu.jdbcorm.type.TypeHandlerRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author bin
 * @since 2023/10/13
 */
@Getter
@Setter
@RequiredArgsConstructor
public class OrmFactory {
    private final DataSource dataSource;
    private TypeHandlerRegistry typeHandlerRegistry = TypeHandlerRegistry.GLOBAL;
    private SqlPrepareRegister sqlPrepareRegister = SqlPrepareRegister.GLOBAL;

    public OrmTransaction newTransaction() throws SqlException {
        return newTransaction(false);
    }

    public OrmTransaction newTransaction(boolean autoCommit) throws SqlException {
        try {
            return new OrmTransaction(
                    dataSource.getConnection(),
                    typeHandlerRegistry,
                    sqlPrepareRegister,
                    autoCommit);
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public OrmTransaction newTransaction(DataSource dataSource) throws SqlException {
        try {
            return new OrmTransaction(
                    dataSource.getConnection(),
                    typeHandlerRegistry,
                    sqlPrepareRegister,
                    false
            );
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }
}
