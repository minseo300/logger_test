package dbappender_proto2;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private Info info = Info.getInstance();
    private static HikariConfig config = new HikariConfig();
    public static HikariDataSource ds;
    private static ConnectionPool instance = new ConnectionPool();

    public ConnectionPool() {
        config.setDriverClassName(info.driver);
        config.setUsername(info.user);
        config.setPassword(info.password);
        config.setJdbcUrl(info.url);

        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public static ConnectionPool getInstance() {
        return instance;
    }
}


