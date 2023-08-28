package dbappender_proto3;



import dbappender_proto3.column_converter.ColumnConverter;
import dbappender_proto3.column_converter.DefaultColumnConverter;
import dbappender_proto3.sqlDialect.CustomSQLDialect;
import dbappender_proto3.sqlDialect.DB2Dialect;
import dbappender_proto3.sqlDialect.MySQLDialect;
import dbappender_proto3.sqlDialect.OracleDialect;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Info {
    public String user;
    public String password;
    public String url;
    public String driver;
    public String tableName;
    public String appenderName;
    public String pattern;
    public ColumnConverter columnConverter;
    public String framework;
    public CustomSQLDialect sqlDialect;

    private static Info info=new Info();

    private Info(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("/Users/iminseo/Desktop/JAVA/logger_test/src/main/resources/db-appender.properties"));
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            url = properties.getProperty("url");
            driver = properties.getProperty("driver");

            tableName = properties.getProperty("tableName");
            appenderName = properties.getProperty("appenderName");
            pattern = properties.getProperty("pattern");
            framework = properties.getProperty("framework");
            String converterClassName = properties.getProperty("columnConverter");
            if(converterClassName.equals("null")) {
                columnConverter = new DefaultColumnConverter();
            } else {
                Object converterClass = Class.forName(converterClassName).newInstance();
                columnConverter = (ColumnConverter) converterClass;
            }
            if (driver.contains("mysql")) {
                sqlDialect = new MySQLDialect();
            } else if (driver.contains("oracle")) {
                sqlDialect = new OracleDialect();
            } else if (driver.contains("db2")) {
                sqlDialect = new DB2Dialect();
            }
            sqlDialect.registerColumnConverter(tableName, columnConverter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static Info getInstance(){
        return info;
    }

}
