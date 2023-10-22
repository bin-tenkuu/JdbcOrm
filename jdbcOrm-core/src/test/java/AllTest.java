import com.github.bintenkuu.jdbcorm.interfaces.BaseColumn;
import com.github.bintenkuu.jdbcorm.interfaces.BaseTable;
import com.github.bintenkuu.jdbcorm.table.OrmFactory;
import com.github.bintenkuu.jdbcorm.type.ObjectTypeHandler;
import com.github.bintenkuu.jdbcorm.util.TableUtil;
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
    private static OrmFactory ormFactory;

    private static final BaseColumn<TestTable, Long> ID = TableUtil.columnId(TestTable::setId);
    private static final BaseColumn<TestTable, String> NAME = TableUtil.columnName(TestTable::setName);
    private static final BaseTable<TestTable> TABLE = TableUtil.of(TestTable::new, ID, NAME);

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class TestTable {
        private Long id;
        private String name;
    }

    @BeforeClass
    public static void init() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:db.sqlite3");
        ormFactory = new OrmFactory(dataSource);
        try (val transaction = ormFactory.newTransaction()) {
            transaction.execute("""
                        CREATE TABLE IF NOT EXISTS test_table (
                            id INTEGER PRIMARY KEY AUTOINCREMENT ,
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
        try (val transaction = ormFactory.newTransaction()) {
            val expression = transaction.process("insert into test_table (name) values (?)");
            int batch = 0;
            for (int i = 1; i < 100000; i++, batch++) {
                expression.setParams(i);
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
        try (val transaction = ormFactory.newTransaction()) {
            val expression = transaction
                    .process("select name from test_table where id = ?");
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
        try (val transaction = ormFactory.newTransaction()) {
            val list = transaction
                    .process("select * from test_table where id = 0")
                    .executeQuery(TABLE);
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
        try (val transaction = ormFactory.newTransaction()) {
            val list = transaction
                    .process("select * from test_table where id = 0")
                    .executeQuery(ID);
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
