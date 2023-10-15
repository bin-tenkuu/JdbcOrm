import lombok.val;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author bin
 * @version 1.0.0
 * @since 2023/10/14
 */
public class SwatchSpeedTest {
    private static final Map<Class<?>, Consumer<Object>> map = new HashMap<>();
    private static Consumer<Object> consumer;
    private static final List<Object> list = new ArrayList<>();
    private static Consumer<Object> out = o -> {
    };
    @Rule
    public CustomStopwatch stopwatch = new CustomStopwatch();

    @BeforeClass
    public static void init() {
        map.put(Boolean.class, out);
        map.put(Byte.class, out);
        map.put(Short.class, out);
        map.put(Character.class, out);
        map.put(Integer.class, out);
        map.put(Long.class, out);
        map.put(Float.class, out);
        map.put(Double.class, out);
        map.put(String.class, out);
        map.put(BigDecimal.class, out);
        map.put(BigInteger.class, out);
        map.put(byte[].class, out);
        map.put(java.sql.Date.class, out);
        map.put(java.sql.Time.class, out);
        map.put(java.sql.Timestamp.class, out);

        consumer = o -> {
            switch (o) {
                case Boolean v -> out.accept("Boolean");
                case Byte v -> out.accept("Byte");
                case Short v -> out.accept("Short");
                case Character v -> out.accept("Character");
                case Integer v -> out.accept("Integer");
                case Long v -> out.accept("Long");
                case Float v -> out.accept("Float");
                case Double v -> out.accept("Double");
                case String v -> out.accept("String");
                case BigDecimal v -> out.accept("BigDecimal");
                case BigInteger v -> out.accept("BigInteger");
                case byte[] v -> out.accept("byte[]");
                case java.sql.Date v -> out.accept("java.sql.Date");
                case java.sql.Time v -> out.accept("java.sql.Time");
                case java.sql.Timestamp v -> out.accept("java.sql.Timestamp");
                case null -> out.accept("null");
                default -> throw new IllegalStateException("Unexpected value: " + o);
            }
        };

        list.add(true);
        list.add((byte) 1);
        list.add((short) 1);
        list.add('a');
        list.add(1);
        list.add(1L);
        list.add(1.0F);
        list.add(1.0);
        list.add("1");
        list.add(new BigDecimal("1"));
        list.add(new BigInteger("1"));
        list.add(new byte[]{1});
        list.add(new java.sql.Date(1L));
        list.add(new java.sql.Time(1L));
        list.add(new java.sql.Timestamp(1L));
        list.add(null);
        val test = new SwatchSpeedTest();
        for (int i = 0; i < 2; i++) {
            test.test1();
            test.test2();
        }
    }

    /**
     * first time < 1ms
     */
    @Test
    public void test1() {
        for (Object o : list) {
            if (o == null) {
                out.accept("null");
            } else {
                map.get(o.getClass()).accept(o);
            }
        }
    }

    /**
     * first time = 12 ms
     */
    @Test
    public void test2() {
        for (Object o : list) {
            consumer.accept(o);
        }
    }

}
