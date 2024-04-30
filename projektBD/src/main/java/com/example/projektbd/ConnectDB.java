package com.example.projektbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    //public static void main(String[] args) {
    static String jdbcUrl = "jdbc:postgresql://localhost:5432/tanksdb";
        static String username = "postgres";
        static String password = "admin";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Połączono z bazą danych");
        } catch (SQLException e) {
            // Log the error or display a user-friendly message
            System.err.println("Nieudane połączenie z bazą danych");
            e.printStackTrace();
        }
        return connection;
    }
}
