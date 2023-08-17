package dbappender_proto.sqlDialect;

import dbappender_proto.LoggerConfigInfo;

public class MySQLDialect implements CustomSQLDialect {
    public static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private LoggerConfigInfo loggerConfigInfo;
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

}
