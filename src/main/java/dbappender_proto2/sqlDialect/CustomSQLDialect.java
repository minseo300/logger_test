package dbappender_proto2.sqlDialect;

import java.util.Map;

public interface CustomSQLDialect {
    String getSelectInsertId();
    String getTableExistsQuery(String tableName);
    String getCreateTableQuery(String tableName, Map<String, String> columns);
    String getInsertSQL(String tableName);
    void createInsertSQL(String tableName, Map<String, String> columnPatternMap, Map<String, Integer> patternIndexMap);

}
