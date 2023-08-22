package dbappender_proto.sqlDialect;

import dbappender_proto.LoggerConfigInfo;

import java.util.Map;

public interface CustomSQLDialect {
    String getSelectInsertId();
    boolean checkTableExists(String tableName);
    String createTableSQL(String tableName, LoggerConfigInfo loggerConfigInfo);
    String getInsertSQL();
    Map<String, Integer> getIndexMap();
}
