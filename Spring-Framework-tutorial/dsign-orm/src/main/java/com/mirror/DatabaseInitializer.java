package com.mirror;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author mirror
 */
@Component
public class DatabaseInitializer {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @PostConstruct
    public void init() throws SQLException {
        try (var conn = dataSource.getConnection()) {
            try (var stmt = conn.createStatement()) {
                jdbcTemplate.update("DROP TABLE IF EXISTS users");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" //
                        + "id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY, " //
                        + "email VARCHAR(100) NOT NULL, " //
                        + "password VARCHAR(100) NOT NULL, " //
                        + "name VARCHAR(100) NOT NULL, " //
                        + "createdAt BIGINT NOT NULL, " //
                        + "UNIQUE (email))");
            }
        }
    }
}
