package dbappender_proto.sqlDialect;


import dbappender_proto.LoggerConfigInfo;

public class DB2Dialect implements CustomSQLDialect {
    @Override
    public String getSelectInsertId() {
        return null;
    }

    @Override
    public boolean checkTableExists(String tableName) {
        return false;
    }

    @Override
    public String createTableSQL(String tableName, LoggerConfigInfo loggerConfigInfo) {
        return null;
    }
}
