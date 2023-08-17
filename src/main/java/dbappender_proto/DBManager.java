package dbappender_proto;

import dbappender_proto.sqlDialect.CustomSQLDialect;
import dbappender_proto.sqlDialect.MySQLDialect;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBManager {
    private String tableName = null;
    private CustomSQLDialect dialect = null;
    private LoggerConfigInfo loggerConfigInfo;
    Statement statement = null;
    public DBManager(LoggerConfigInfo loggerConfigInfo) {
        this.loggerConfigInfo = loggerConfigInfo;
    }
    public void createConnectionPool(){
        Properties properties = new Properties();
        String url = null, driver = null, user = null, password = null, appenderName=null;

        try {
            properties.load(new FileInputStream("/Users/iminseo/Desktop/JAVA/logger_test/src/main/resources/db-appender.properties"));
            url = properties.getProperty("url");
            driver = properties.getProperty("driver");
            setDialect(driver);
            user = properties.getProperty("username");
            password = properties.getProperty("password");
            tableName = properties.getProperty("tableName");
            appenderName = properties.getProperty("appenderName");
            LogConnectionPool connectionPool = new LogConnectionPool(url, driver, user, password);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setDialect(String driver) {
        if(driver.contains("mysql")) {
            dialect = new MySQLDialect();
        }
    }
    public void checkTableExist() {
        boolean isExist = dialect.checkTableExists(tableName);
        String createTableSql;
        if(!isExist) {
            createTableSql = dialect.createTableSQL(tableName, loggerConfigInfo);
            Connection connection = LogConnectionPool.getConnection();
            try {
                statement = connection.createStatement();
                statement.execute(createTableSql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
