package dbappender_proto3.sqlDialect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OracleDialect implements CustomSQLDialect{
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
//        String sequence = "CREATE SEQUENCE "+tableName+"_sequence MINVALUE 1 START WITH 1;";
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ");
        sqlBuilder.append(tableName);
        sqlBuilder.append(" (");
        for (int i = 1 ; i <columnNameList.size() ; i++) {
            sqlBuilder.append(columnNameList.get(i));
            sqlBuilder.append(" VARCHAR(254), ");
        }
        sqlBuilder.delete(sqlBuilder.length()-2,sqlBuilder.length());
        sqlBuilder.append(")");
//        sqlBuilder.append(" ID NUMBER(10) NOT NULL, ");
//        sqlBuilder.append("CONSTRAINT ID PRIMARY KEY);");
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
        for (int i = 1; i < columnNameList.size() ; i++){
            sqlBuilder.append(columnNameList.get(i)).append(", ");
        }
        sqlBuilder.delete(sqlBuilder.length()-2,sqlBuilder.length());
        sqlBuilder.append(") VALUES (");
        for (int i = 1; i < columnNameList.size() ; i++) {
            sqlBuilder.append("?, ");
        }
        sqlBuilder.delete(sqlBuilder.length()-2,sqlBuilder.length());
        sqlBuilder.append(")");
        insertQueryMap.put(tableName,sqlBuilder.toString());
    }
}
