package dbappender_proto.sqlDialect;


import dbappender_proto.LoggerConfigInfo;

import java.util.Map;

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

    @Override
    public String getInsertSQL() {
        return null;
    }

    @Override
    public Map<String, Integer> getIndexMap() {
        return null;
    }
}
