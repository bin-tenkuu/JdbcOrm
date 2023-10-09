package com.github.bintenkuu.jdbcorm.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author bin
 * @since 2023/10/08
 */
public class InstantTypeHandler implements TypeHandler<Instant> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Instant parameter) throws SQLException {
        ps.setTimestamp(i, Timestamp.from(parameter));
    }

    @Override
    public Instant getResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnIndex);
        return getInstant(timestamp);
    }

    private static Instant getInstant(Timestamp timestamp) {
        if (timestamp != null) {
            return timestamp.toInstant();
        }
        return null;
    }
}
