package dbappender_proto.sqlDialect;

import dbappender_proto.LoggerConfigInfo;

public interface CustomSQLDialect {
    String getSelectInsertId();
    boolean checkTableExists(String tableName);
    String createTableSQL(String tableName, LoggerConfigInfo loggerConfigInfo);
}
