package com.mirror.JDBC.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mirror
 */
public class ConnectMySQL {
    String JDBC_URL = "jdbc:mysql://localhost:3306/example_tutorial?useSSL=false&allowPublicKeyRetrieval=true";
    String JDBC_USER = "root";
    String JDBC_PASSWORD = "root";
    public Connection connect(){
        try {
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
