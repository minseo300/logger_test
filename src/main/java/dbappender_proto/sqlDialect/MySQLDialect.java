package dbappender_proto.sqlDialect;

import dbappender_proto.LoggerConfigInfo;

import java.util.HashMap;
import java.util.Map;

public class MySQLDialect implements CustomSQLDialect {
    public static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private LoggerConfigInfo loggerConfigInfo;
    int count=0;
    Map<String, Integer> indexMap = new HashMap<>();

    public String getSelectInsertId() {
        return SELECT_LAST_INSERT_ID;
    }

    @Override
    public boolean checkTableExists(String tableName) {
        return false;
    }

    public String createTableSQL(String tableName, LoggerConfigInfo loggerConfigInfo) {
        this.loggerConfigInfo = loggerConfigInfo;
        StringBuilder createTableQuery = new StringBuilder();
        createTableQuery.append("create table ");
        createTableQuery.append(tableName);

        if(loggerConfigInfo.getLogDate()) {
            createTableQuery.append("(LOG_DATE TIMESTAMP, ");
        }
        if(loggerConfigInfo.getLogLevel()) {
            createTableQuery.append("LEVEL VARCHAR(254), ");
        }
        if(loggerConfigInfo.getLogId()) {
            createTableQuery.append("EVENT_ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
        }
        if(loggerConfigInfo.getMessage()) {
            createTableQuery.append("MESSAGE VARCHAR(254), ");
        }
        if(loggerConfigInfo.getLoggerName()) {
            createTableQuery.append("LOGGER_NAME VARCHAR(254), ");
        }
        if(loggerConfigInfo.getException()) {
            createTableQuery.append("EXCEPTION VARCHAR(254));");
        }
        return createTableQuery.toString();
    }

    public String getInsertSQL() {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        sqlBuilder.append(this.loggerConfigInfo.getTableName()).append(" (");
        if(loggerConfigInfo.getLogDate()) {
            sqlBuilder.append("LOG_DATE, ");
            count++;
            indexMap.put("LOG_DATE",count);
        }
        if(loggerConfigInfo.getLogLevel()) {
            sqlBuilder.append("LEVEL, ");
            count++;
            indexMap.put("LEVEL",count);

        }
        if(loggerConfigInfo.getMessage()) {
            sqlBuilder.append("MESSAGE, ");
            count++;
            indexMap.put("MESSAGE",count);

        }
        if(loggerConfigInfo.getLoggerName()) {
            sqlBuilder.append("LOGGER_NAME, ");
            count++;
            indexMap.put("LOGGER_NAME",count);

        }
        if(loggerConfigInfo.getException()) {
            sqlBuilder.append("EXCEPTION, ");
            count++;
            indexMap.put("EXCEPTION", count);
        }
        sqlBuilder.delete(sqlBuilder.length()-2,sqlBuilder.length());
        sqlBuilder.append(") VALUES (");
        for (int i=0 ; i<count ; i++) {
            sqlBuilder.append("?, ");
        }
        sqlBuilder.delete(sqlBuilder.length()-2,sqlBuilder.length());
        sqlBuilder.append(")");
        count = 0;
        return sqlBuilder.toString();
    }

    public Map<String, Integer> getIndexMap() {
        return this.indexMap;
    }

}
