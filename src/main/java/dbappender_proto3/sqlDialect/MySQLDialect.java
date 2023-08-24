package dbappender_proto3.sqlDialect;

import java.util.*;

public class MySQLDialect implements CustomSQLDialect {

    private Map<String, String> insertQueryMap = new HashMap<>();
    private Map<String, String> createQueryMap = new HashMap<>();

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
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS ");
        sqlBuilder.append(tableName);
        sqlBuilder.append(" (");
        for (int i = 0 ; i <columnNameList.size() ; i++) {
            sqlBuilder.append(columnNameList.get(i));
            sqlBuilder.append(" VARCHAR(254), ");
        }
        sqlBuilder.append("ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY);");
        String createTableQuery = sqlBuilder.toString();
        createQueryMap.put(tableName,createTableQuery);
    }

    @Override
    public String getCreateTableQuery(String tableName) {
        String createQuery = createQueryMap.get(tableName);
        return createQuery;
    }

    @Override
    public String getInsertSQL(String tableName) {
        String insertSQL = insertQueryMap.get(tableName);

        return insertSQL;
    }

    @Override
    public void createInsertSQL(String tableName, List<String> columnNameList) {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        sqlBuilder.append(tableName).append(" (");
        for (int i = 0; i < columnNameList.size() ; i++) {
            sqlBuilder.append(columnNameList.get(i)).append(", ");
        }
        sqlBuilder.delete(sqlBuilder.length()-2,sqlBuilder.length());
        sqlBuilder.append(") VALUES (");
        for (int i = 0; i < columnNameList.size() ; i++) {
            sqlBuilder.append("?, ");
        }
        sqlBuilder.delete(sqlBuilder.length()-2,sqlBuilder.length());
        sqlBuilder.append(")");
        insertQueryMap.put(tableName,sqlBuilder.toString());
    }

}
