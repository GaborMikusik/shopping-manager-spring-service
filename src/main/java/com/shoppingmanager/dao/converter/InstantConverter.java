package com.shoppingmanager.dao.converter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class InstantConverter {
    public static Instant convertToInstant(ResultSet rs, String columnName) throws SQLException {
        Object object = rs.getObject(columnName);
        if (object instanceof java.sql.Timestamp)
            return ((java.sql.Timestamp) object).toInstant();
        if (object instanceof java.sql.Date)
            return ((java.sql.Date) object).toLocalDate().atStartOfDay().toInstant((ZoneOffset) ZoneId.of("UTC"));
        if (object instanceof LocalDateTime)
            return ((LocalDateTime) object).atZone(ZoneId.systemDefault()).toInstant();

        throw new IllegalArgumentException("Unsupported data type for column: " + columnName);
    }
}
