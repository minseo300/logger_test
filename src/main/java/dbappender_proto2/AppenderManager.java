package dbappender_proto2;

import dbappender_proto2.column_converter.ColumnFactory;
import dbappender_proto2.column_converter.Log4j2ColumnFactory;
import dbappender_proto2.column_converter.LogbackColumnFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class AppenderManager {
    private Info info = Info.getInstance();
    private Map<String, Object> appenderRegistry = new HashMap<>();
    public LogbackFactory logbackFactory = new LogbackFactory();
    public Log4j2Factory log4j2Factory = new Log4j2Factory();
    public ColumnFactory columnFactory;

    private static AppenderManager appenderManager = new AppenderManager();

    private AppenderManager() {
        if(info.framework.equals("logback")){
            columnFactory = new LogbackColumnFactory();
        } else {
            columnFactory = new Log4j2ColumnFactory();
        }
        columnFactory.mappingColumn(info.pattern, info.tableName);
    }
    public static AppenderManager getInstance() {
        return appenderManager;
    }
    public Map<String, Object> getAppenderRegistry() {
        return this.appenderRegistry;
    }
    public Object getAppender(String appenderName) {
        return appenderRegistry.get(appenderName);
    }
    public void createAppender() {
        createTable();
        Object appender = null;
        if (info.framework.equals("log4j2")) {
            appender = log4j2Factory.createDBAppender();
        } else if(info.framework.equals("logback")){
            appender = logbackFactory.createDBAppender((LogbackColumnFactory) columnFactory);
        }
        appenderRegistry.put(info.appenderName,appender);
    }

    private void createTable() {
        String createTableSQL = info.sqlDialect.getCreateTableQuery(info.tableName, columnFactory.getColumnPatternMap(info.tableName));
        info.sqlDialect.createInsertSQL(info.tableName, columnFactory.getColumnPatternMap(info.tableName), columnFactory.getPatternIndexMap(info.tableName));
        Connection connection = ConnectionPool.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
