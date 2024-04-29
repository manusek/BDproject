package com.example.projektbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    static String jdbcUrl = "jdbc:postgresql://localhost:5432/tanksdb";
    static String username = "postgres";
    static String password = "admin";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            // Tutaj możesz obsłużyć wyjątek w odpowiedni sposób, np. poinformować użytkownika o błędzie
        }
        return connection;
    }
}
