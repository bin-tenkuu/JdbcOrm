import com.github.bintenkuu.jdbcorm.table.Column;
import com.github.bintenkuu.jdbcorm.table.OrmFactory;
import com.github.bintenkuu.jdbcorm.table.Table;
import com.github.bintenkuu.jdbcorm.table.OrmTransaction;
import com.github.bintenkuu.jdbcorm.type.ObjectTypeHandler;
import lombok.*;
import org.junit.*;
import org.sqlite.SQLiteDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author bin
 * @since 2023/10/09
 */
public class AllTest {

    @Rule
    public CustomStopwatch stopwatch = new CustomStopwatch();
    private static SQLiteDataSource dataSource;
    private static OrmFactory ormFactory;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class TestTable {
        private Long id;
        public static final Column<TestTable, Long> ID = Column.id(TestTable::setId);
        private String name;
        public static final Column<TestTable, String> NAME = Column.name(TestTable::setName);
        public static final Table<TestTable> TABLE = Table.of(TestTable::new, ID, NAME);
    }

    @BeforeClass
    public static void init() {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:db.sqlite3");
        try (val transaction = OrmTransaction.of(dataSource)) {
            transaction.execute("""
                        CREATE TABLE IF NOT EXISTS test_table (
                            id INTEGER PRIMARY KEY,
                            name TEXT NOT NULL
                        )
                    """);
            transaction.execute("insert into test_table(id, name) values (0, 0)");
            transaction.commit();
        }
    }

    @AfterClass
    public static void last() throws IOException {
        Files.deleteIfExists(Paths.get("db.sqlite3"));
    }

    @Test
    public void testBatch() {
        try (val transaction = OrmTransaction.of(dataSource)) {
            val expression = transaction
                    . "insert OR ignore into test_table (\{ TestTable.TABLE.all() }) values (\{ TestTable.ID }, \{ TestTable.NAME })" ;
            int batch = 0;
            for (int i = 1; i < 100000; i++, batch++) {
                expression.setParams((long) i, String.valueOf(i));
                if (batch >= 512) {
                    expression.executeUpdateBatch();
                    expression.clearBatch();
                    batch = 0;
                } else {
                    expression.addBatch();
                }
            }
            if (batch > 0) {
                expression.executeUpdate();
            }
            transaction.commit();
        }

    }

    @Test
    public void testTypeHandler() {
        try (val transaction = OrmTransaction.of(dataSource)) {
            val expression = transaction
                    . "select name from test_table where id = \{ Integer.class }" ;
            expression.setParams(0);
            val list = expression.executeQuery(new ObjectTypeHandler());
            Assert.assertEquals(
                    "size is not 1",
                    1, list.size()
            );
            Assert.assertEquals(
                    "0", list.get(0)
            );
        }
    }

    @Test
    public void testTable() {
        try (val transaction = OrmTransaction.of(dataSource)) {
            val list = transaction
                    . "select * from test_table where id = \{ 0 }"
                    .executeQuery(TestTable.TABLE);
            Assert.assertEquals(
                    "size is not 1",
                    1, list.size()
            );
            Assert.assertEquals(
                    new TestTable(0L, "0"),
                    list.get(0)
            );
        }
    }

    @Test
    public void testColumn() {
        try (val transaction = OrmTransaction.of(dataSource)) {
            val list = transaction
                    . "select * from test_table where id = \{ 0 }"
                    .executeQuery(TestTable.ID);
            Assert.assertEquals(
                    "size is not 1",
                    1, list.size()
            );
            Assert.assertEquals(
                    Long.valueOf(0L), list.get(0)
            );
        }
    }

}
