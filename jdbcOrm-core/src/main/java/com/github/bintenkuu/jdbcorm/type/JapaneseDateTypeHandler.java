package com.github.bintenkuu.jdbcorm.type;

import com.github.bintenkuu.jdbcorm.interfaces.TypeHandler;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;

/**
 * @author bin
 * @since 2023/10/08
 */
public class JapaneseDateTypeHandler implements TypeHandler<JapaneseDate> {
    @Override
    public void setParameter(PreparedStatement ps, int i, JapaneseDate parameter) throws SQLException {
        ps.setDate(i, Date.valueOf(LocalDate.ofEpochDay(parameter.toEpochDay())));
    }

    @Override
    public JapaneseDate getResult(ResultSet rs, int columnIndex) throws SQLException {
        Date date = rs.getDate(columnIndex);
        return getJapaneseDate(date);
    }

    private static JapaneseDate getJapaneseDate(Date date) {
        if (date != null) {
            return JapaneseDate.from(date.toLocalDate());
        }
        return null;
    }

}
