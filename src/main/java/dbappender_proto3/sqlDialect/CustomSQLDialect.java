package dbappender_proto3.sqlDialect;

import ch.qos.logback.core.db.dialect.SQLDialect;
import dbappender_proto3.column_converter.ColumnConverter;

import java.util.List;
import java.util.Map;

public interface CustomSQLDialect extends SQLDialect {
    String getSelectInsertId();
    String getTableExistsQuery(String tableName);
    void createTableQuery(String tableName, List<String> columnNameList);
    List<String> getCreateTableQuery(String tableName);
    String getInsertSQL(String tableName);
    void createInsertSQL(String tableName, List<String> columnNameList);
    void registerColumnConverter(String tableName, ColumnConverter converter);

}
