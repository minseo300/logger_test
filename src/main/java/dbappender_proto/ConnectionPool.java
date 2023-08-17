package dbappender_proto;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPool {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    private static ConnectionPool instance = null;
    public String tableName;


    public ConnectionPool() {
        Properties properties = new Properties();
        String url = null, driver = null, user = null, password = null;
        try {
            properties.load(new FileInputStream("/Users/iminseo/Desktop/JAVA/logger_test/src/main/resources/db-appender.properties"));
            url = properties.getProperty("url");
            driver = properties.getProperty("driver");
            user = properties.getProperty("username");
            password = properties.getProperty("password");
            this.tableName = properties.getProperty("tableName");

//            System.out.println(properties.getProperty("driver"));
//            System.out.println(properties.getProperty("username"));
//            System.out.println(properties.getProperty("password"));
//            System.out.println(properties.getProperty("url"));
//            System.out.println(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName(driver);

        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return ds.getConnection();
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }
}
