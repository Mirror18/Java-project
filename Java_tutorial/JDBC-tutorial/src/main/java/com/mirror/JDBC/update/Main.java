package com.mirror.JDBC.update;

import com.mirror.JDBC.connect.ConnectMySQL;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author mirror
 */
public class Main {
    public static void main(String[] args) {
        Connection connection = new ConnectMySQL().connect();
        String insertSql = "INSERT INTO students (id, grade, name, gender) VALUES (?,?,?,?)";
        insert(connection,insertSql);

        String insertSqlKey = "INSERT INTO students ( grade, name, gender) VALUES (?,?,?)";
        insertOfKeys(connection,insertSqlKey);
    }
//更新和删除都是一样的操作，除了sql语句不同
    @SneakyThrows
    public static void insert(Connection connection, String s) {
        try (PreparedStatement ps = connection.prepareStatement(s)) {
            ps.setObject(1, 999); // 注意：索引从1开始
            ps.setObject(2, 1); // grade
            ps.setObject(3, "Bob"); // name
            ps.setObject(4, "2"); // gender
            int n = ps.executeUpdate(); // 1
            System.out.println(n);

        }
    }

    @SneakyThrows
    public static void insertOfKeys(Connection connection, String s){
        try(PreparedStatement ps = connection.prepareStatement(s, Statement.RETURN_GENERATED_KEYS)){
            ps.setObject(1,1);
            ps.setObject(2,"mirror");
            ps.setObject(3,0);
            int n = ps.executeUpdate();
            System.out.println(n);
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    long id = rs.getLong(1);
                    System.out.println(id);
                }
            }
        }
    }


}
