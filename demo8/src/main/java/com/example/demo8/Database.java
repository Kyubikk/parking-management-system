package com.example.demo8;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=pms3;user=sa;password=Linh0711;");
    }
}
