package com.github.bintenkuu.jdbcorm.type;

import lombok.val;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.*;
import java.time.chrono.JapaneseDate;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author bin
 * @since 2023/08/31
 */
public class TypeHandlerRegistry {
    public static final TypeHandlerRegistry GLOBAL = new TypeHandlerRegistry();
    private final HashMap<Type, TypeHandler<?>> allTypeHandlersMap = new HashMap<>();
    private final HashSet<Class<?>> allClassTypes = new HashSet<>();

    static {
        GLOBAL.register(Object.class, new ObjectTypeHandler());
        GLOBAL.register(Boolean.class, boolean.class, new BooleanTypeHandler());
        GLOBAL.register(Byte.class, byte.class, new ByteTypeHandler());
        GLOBAL.register(Character.class, char.class, new CharacterTypeHandler());
        GLOBAL.register(Short.class, short.class, new ShortTypeHandler());
        GLOBAL.register(Integer.class, int.class, new IntegerTypeHandler());
        GLOBAL.register(Long.class, long.class, new LongTypeHandler());
        GLOBAL.register(Float.class, float.class, new FloatTypeHandler());
        GLOBAL.register(Double.class, double.class, new DoubleTypeHandler());
        GLOBAL.register(String.class, new StringTypeHandler());
        GLOBAL.register(BigInteger.class, new BigIntegerTypeHandler());
        GLOBAL.register(BigDecimal.class, new BigDecimalTypeHandler());
        GLOBAL.register(byte[].class, new ByteArrayTypeHandler());
        GLOBAL.register(java.sql.Date.class, new SqlDateTypeHandler());
        GLOBAL.register(java.sql.Time.class, new SqlTimeTypeHandler());
        GLOBAL.register(java.sql.Timestamp.class, new SqlTimestampTypeHandler());

        GLOBAL.register(Instant.class, new InstantTypeHandler());
        GLOBAL.register(LocalDateTime.class, new LocalDateTimeTypeHandler());
        GLOBAL.register(LocalDate.class, new LocalDateTypeHandler());
        GLOBAL.register(LocalTime.class, new LocalTimeTypeHandler());
        GLOBAL.register(OffsetDateTime.class, new OffsetDateTimeTypeHandler());
        GLOBAL.register(OffsetTime.class, new OffsetTimeTypeHandler());
        GLOBAL.register(ZonedDateTime.class, new ZonedDateTimeTypeHandler());
        GLOBAL.register(Month.class, new MonthTypeHandler());
        GLOBAL.register(Year.class, new YearTypeHandler());
        GLOBAL.register(YearMonth.class, new YearMonthTypeHandler());
        GLOBAL.register(JapaneseDate.class, new JapaneseDateTypeHandler());
    }

    public TypeHandlerRegistry() {
    }

    public <T> void register(Class<T> clazz, TypeHandler<T> typeHandler) {
        allTypeHandlersMap.put(clazz, typeHandler);
        allClassTypes.add(clazz);
    }

    public <T> void register(Type type, TypeHandler<T> typeHandler) {
        allTypeHandlersMap.put(type, typeHandler);
    }

    public <T> void register(Class<T> clazz, Class<T> primitiveClazz, TypeHandler<T> typeHandler) {
        register(clazz, typeHandler);
        register(primitiveClazz, typeHandler);
    }

    public <T> TypeHandler<T> getTypeHandler(Type type) {
        return getTypeHandler(type, 0);
    }

    public <T> TypeHandler<T> getTypeHandler(Class<T> clazz) {
        return getTypeHandler(clazz, 0);
    }

    /**
     * @param type {@link java.lang.reflect.Type}
     * @param code {@link java.sql.Types}
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> TypeHandler<T> getTypeHandler(Type type, int code) {
        TypeHandler<T> handler = (TypeHandler<T>) allTypeHandlersMap.get(type);
        if (handler != null) {
            return handler;
        }
        if (type instanceof Class<?> type1) {
            if (Enum.class.isAssignableFrom(type1)) {
                return new EnumNameTypeHandler<>((Class) type1);
            }
            for (Class<?> aClass : allClassTypes) {
                if (type1.isAssignableFrom(aClass)) {
                    return (TypeHandler<T>) allTypeHandlersMap.get(type);
                }
            }
        } else if (type instanceof ParameterizedType type1) {
            return getTypeHandler(type1.getActualTypeArguments()[0], code);
        }
        throw new UnsupportedOperationException("no type handler found for type " + type);
    }
}
