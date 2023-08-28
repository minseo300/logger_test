package dbappender_proto3.sqlDialect;

import dbappender_proto3.column_converter.ColumnConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OracleDialect implements CustomSQLDialect{
    private Map<String, String> insertQueryMap = new HashMap<>();
    private Map<String, List<String>> createQueryMap = new HashMap<>();
    private Map<String, ColumnConverter> columnConverterMap = new HashMap<>();


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
        List<String> forCreateTable = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        StringBuilder sequenceBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ");
        sqlBuilder.append(tableName);
        sqlBuilder.append(" (");
        for (int i = 0 ; i <columnNameList.size() ; i++) {
            sqlBuilder.append(columnNameList.get(i));
            sqlBuilder.append(" VARCHAR(254), ");
        }
        ColumnConverter columnConverter = columnConverterMap.get(tableName);
        if (columnConverter.getLogIdName() != null) {
            sequenceBuilder.append("CREATE SEQUENCE SEQ_");
            sequenceBuilder.append(tableName);
            sqlBuilder.append(columnConverter.getLogIdName());
            sqlBuilder.append(" NUMBER(10) NOT NULL,");
            sqlBuilder.append("CONSTRAINT ");
            sqlBuilder.append(tableName+"_ID_PK PRIMARY KEY(");
            sqlBuilder.append(columnConverter.getLogIdName());
            sqlBuilder.append("))");
        } else {
            sqlBuilder.delete(sqlBuilder.length()-2,sqlBuilder.length());
            sqlBuilder.append(")");
        }
        String createTableQuery = sqlBuilder.toString();
        String sequencePKQuery = sequenceBuilder.toString();
        forCreateTable.add(createTableQuery);
        forCreateTable.add(sequencePKQuery);
        createQueryMap.put(tableName,forCreateTable);
    }


    @Override
    public List<String> getCreateTableQuery(String tableName) {
        List<String> createQuery = createQueryMap.get(tableName);
        return createQuery;
    }

    @Override
    public String getInsertSQL(String tableName) {
        String insertSQL = insertQueryMap.get(tableName);

        return insertSQL;
    }

    @Override
    public void createInsertSQL(String tableName, List<String> columnNameList) {
        ColumnConverter columnConverter = columnConverterMap.get(tableName);
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
        if (columnConverter.getLogIdName() != null) {
            sqlBuilder.append("SEQ_");
            sqlBuilder.append(tableName);
            sqlBuilder.append(".NEXTVAL");
        } else {
            sqlBuilder.delete(sqlBuilder.length()-2,sqlBuilder.length());
            sqlBuilder.append(")");
        }

        String insertQuery = sqlBuilder.toString();
        insertQueryMap.put(tableName,insertQuery);
    }
    @Override
    public void registerColumnConverter(String tableName, ColumnConverter converter) {
        columnConverterMap.put(tableName, converter);
    }
}
