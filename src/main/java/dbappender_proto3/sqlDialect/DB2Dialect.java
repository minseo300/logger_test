package dbappender_proto3.sqlDialect;

import java.util.List;

public class DB2Dialect implements CustomSQLDialect{
    @Override
    public String getSelectInsertId() {
        return null;
    }

    @Override
    public String getTableExistsQuery(String tableName) {
        return null;
    }

    @Override
    public void createTableQuery(String tableName, List<String> columnNameList) {

    }

    @Override
    public String getCreateTableQuery(String tableName) {
        return null;
    }

    @Override
    public String getInsertSQL(String tableName) {
        return null;
    }

    @Override
    public void createInsertSQL(String tableName, List<String> columnNameList) {

    }
}
