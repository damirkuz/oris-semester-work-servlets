package ru.kuzdikenov.helper;

import ru.kuzdikenov.app.DefaultSettings;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtil {

    public static HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        String url = DefaultSettings.DB_URL;
        String user = DefaultSettings.DB_USER;
        String pass = DefaultSettings.DB_PASSWORD;

        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(pass);
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        return new HikariDataSource(config);
    }


    public interface TransactionalOperation<T> {
        T execute(Connection connection) throws SQLException;
    }

    public static <T> T withTransaction(DataSource dataSource, TransactionalOperation<T> operation) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                T result = operation.execute(connection);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }



}
