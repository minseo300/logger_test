package dbappender_proto;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class LogConnectionPool {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    private static LogConnectionPool instance = null;

    public LogConnectionPool(String url, String driver, String user, String password) {

        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName(driver);

        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            System.out.println("dd");
        }
        return conn;
    }

    public static LogConnectionPool getInstance() {
        return instance;
    }
}
