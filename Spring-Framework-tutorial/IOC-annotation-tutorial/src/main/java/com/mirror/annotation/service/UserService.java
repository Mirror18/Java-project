package com.mirror.annotation.service;


import com.mirror.annotation.bean.User;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mirror
 */
@Component
public class UserService {
    @Autowired
    private MailService mailService;

    @Autowired
    private HikariDataSource dataSource;


//
//    private List<User> users = new ArrayList<>(List.of( // users:
//            new User(1, "bob@example.com", "password", "Bob"), // bob
//            new User(2, "alice@example.com", "password", "Alice"), // alice
//            new User(3, "tom@example.com", "password", "Tom"))); // tom

//    public User login(String email, String password) {
//        for (User user : users) {
//            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
//                mailService.sendLoginMail(user);
//                return user;
//            }
//        }
//        throw new RuntimeException("login failed.");
//    }

    public User login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long dbId = rs.getLong("id");
                    String dbEmail = rs.getString("email");
                    String dbPassword = rs.getString("password");
                    String dbName = rs.getString("name");
                    return new User(dbId, dbEmail, dbPassword, dbName);
                }
            }
        }
        throw new RuntimeException("login failed.");
    }

    public User getUser(long id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long dbId = rs.getLong("id");
                    String dbEmail = rs.getString("email");
                    String dbPassword = rs.getString("password");
                    String dbName = rs.getString("name");
                    return new User(dbId, dbEmail, dbPassword, dbName);
                }
            }
        }
        return null;
    }

    public User register(String email, String password, String name) throws SQLException {
        String sql = "INSERT INTO user (email, password, name) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, name);
            int n = ps.executeUpdate();
            if (n > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        long id = rs.getLong(1);
                        return new User(id, email, password, name);
                    }
                }
            }
        }
        return null;
    }
}