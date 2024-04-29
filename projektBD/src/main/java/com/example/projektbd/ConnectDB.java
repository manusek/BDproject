package com.example.projektbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/tanksdb";
        String username = "postgres";
        String password = "admin";

        //public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Polaczono");
            connection.close();
        } catch (SQLException e) {
            // Log the error or display a user-friendly message
            System.err.println("Niepowodzenie");
            e.printStackTrace();
        }
        //return connection;
        //}
    }
}