package dbappender_proto3.sqlDialect;

import java.util.List;
import java.util.Map;

public interface CustomSQLDialect {
    String getSelectInsertId();
    String getTableExistsQuery(String tableName);
    void createTableQuery(String tableName, List<String> columnNameList);
    String getCreateTableQuery(String tableName);
    String getInsertSQL(String tableName);
    void createInsertSQL(String tableName, List<String> columnNameList);

}
