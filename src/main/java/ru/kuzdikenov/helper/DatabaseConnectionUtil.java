package ru.kuzdikenov.helper;

import ru.kuzdikenov.app.DefaultSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionUtil {
    private static Connection connection;

    public static Connection getConnection() {
        String url = DefaultSettings.DB_URL;
        String user = DefaultSettings.DB_USER;
        String pass = DefaultSettings.DB_PASSWORD;
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, user, pass);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
