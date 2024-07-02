package com.mirror.JDBC.query;

import com.mirror.JDBC.connect.ConnectMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConnectMySQL connectMySQL = new ConnectMySQL();
        try (Connection connection = connectMySQL.connect()) {
            String s = "SELECT id, grade, name, gender FROM students WHERE gender=? AND grade=?";
            try (PreparedStatement ps = connection.prepareStatement(s)) {
                // 注意：索引从1开始
                ps.setObject(1, "M");
                ps.setObject(2, 3);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        long id = rs.getLong("id");
                        long grade = rs.getLong("grade");
                        String name = rs.getString("name");
                        String gender = rs.getString("gender");
                        System.out.println(id + " " + grade + " " + name + " " + gender + " ");
                    }
                }
            }
        }
    }
}
