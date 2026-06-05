package com.expensetracker.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionPool {
    private static HikariDataSource ds;

    static {
        try {
            Properties props = new Properties();
            try (InputStream is = DBConnectionPool.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (is != null) {
                    props.load(is);
                }
            } catch (Exception e) {
                System.err.println("Warning: Could not load application.properties from classpath. Using defaults.");
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url", "jdbc:mysql://localhost:3306/expense_tracker?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"));
            config.setUsername(props.getProperty("db.user", "root"));
            config.setPassword(props.getProperty("db.password", ""));
            config.setDriverClassName(props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver"));
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.max-pool-size", "10")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("db.min-idle", "2")));
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);

            ds = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing connection pool: " + e.getMessage());
        }
    }

    private DBConnectionPool() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
    public static void close() {
        if (ds != null) {
            ds.close();
        }
    }
}
