package com.mirror.JDBC.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author mirror
 */
public class Main {
    @lombok.SneakyThrows
    public static void main(String[] args) {
        // JDBC连接的URL, 不同数据库有不同的格式:
        String JDBC_URL = "jdbc:mysql://localhost:3306/example_tutorial?useSSL=false&allowPublicKeyRetrieval=true";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "root";
// 获取连接:

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
//            try (Statement stmt = conn.createStatement()) {
            //不安全
//                try (ResultSet rs = stmt.executeQuery("SELECT id, grade, name, gender FROM students WHERE gender=1")) {
////                    rs.next()用于判断是否有下一行记录，如果有，将自动把当前行移动到下一行（一开始获得ResultSet时当前行不是第一行）；
//                    while (rs.next()) {
//                        // 注意：索引从1开始
//                        long id = rs.getLong(1);
//                        long grade = rs.getLong(2);
//                        String name = rs.getString(3);
//                        int gender = rs.getInt(4);
//                        System.out.println(id+" "+ grade + " " + name + " " + gender+ " ");
//                    }
//                }
//            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, grade, name, gender FROM students WHERE gender=? AND grade=?")) {
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

