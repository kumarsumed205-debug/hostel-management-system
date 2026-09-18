package com.smarthostel.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static final String URL = System.getenv().getOrDefault(
            "HOSTEL_DB_URL",
            "jdbc:mysql://localhost:3306/smart_hostel?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
    );
    private static final String USER = System.getenv().getOrDefault("HOSTEL_DB_USER", "root");
    private static final String PASSWORD = System.getenv("HOSTEL_DB_PASSWORD");

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found. Add mysql-connector-j to the project.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
