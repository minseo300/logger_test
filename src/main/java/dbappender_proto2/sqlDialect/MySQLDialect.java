package dbappender_proto2.sqlDialect;

import java.util.HashMap;
import java.util.Map;

public class MySQLDialect implements CustomSQLDialect{

    private Map<String, String> insertQueryMap = new HashMap<>();

    @Override
    public String getSelectInsertId() {
        return null;
    }

    @Override
    public String getTableExistsQuery(String tableName) {
        return null;
    }

    @Override
    public String getCreateTableQuery(String tableName, Map<String, String> columns) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS ");
        sqlBuilder.append(tableName);
        sqlBuilder.append(" (");

//        String columnName = null;
        for (String columnName : columns.keySet()) {
//            columnName = columns.get(p);
            sqlBuilder.append(columnName);
            sqlBuilder.append(" VARCHAR(254), ");
        }
        sqlBuilder.append("ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY);");

        return sqlBuilder.toString();
    }

    @Override
    public String getInsertSQL(String tableName) {
        String insertSQL = insertQueryMap.get(tableName);

        return insertSQL;
    }



    public void createInsertSQL(String tableName, Map<String, String> columnPatternMap, Map<String, Integer> patternIndexMap) {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        sqlBuilder.append(tableName).append(" (");
        int index = 1;
        String[] columnList = new String[patternIndexMap.size()+1];
        for (String column : columnPatternMap.keySet()) {
            String pattern = columnPatternMap.get(column);
            int idx = patternIndexMap.get(pattern);
            columnList[idx] = column;
        }
        for (int i = 1 ; i < columnList.length ; i++) {
            sqlBuilder.append(columnList[i]).append(", ");
        }
        sqlBuilder.delete(sqlBuilder.length()-2,sqlBuilder.length());
        sqlBuilder.append(") VALUES (");

        for (int i = 1; i < columnList.length ; i++) {
            sqlBuilder.append("?, ");
        }
        sqlBuilder.delete(sqlBuilder.length()-2,sqlBuilder.length());
        sqlBuilder.append(")");
        insertQueryMap.put(tableName,sqlBuilder.toString());
    }
}
