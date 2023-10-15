/**
 * @author bin
 * @since 2023/10/15
 */
module jdbcOrm.core {
    requires static lombok;
    requires static org.jetbrains.annotations;

    requires transitive java.sql;

    exports com.github.bintenkuu.jdbcorm.table;
    exports com.github.bintenkuu.jdbcorm.type;

    uses com.github.bintenkuu.jdbcorm.type.ResultSetHandler;
    uses com.github.bintenkuu.jdbcorm.type.TypeHandler;

    opens com.github.bintenkuu.jdbcorm.type;
}
