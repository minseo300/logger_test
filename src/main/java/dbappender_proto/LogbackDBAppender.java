package dbappender_proto;

import ch.qos.logback.classic.db.names.DBNameResolver;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.db.DBAppenderBase;
import ch.qos.logback.core.db.DBHelper;
import ch.qos.logback.core.db.dialect.SQLDialectCode;
import dbappender_proto.sqlDialect.CustomSQLDialect;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public class LogbackDBAppender extends DBAppenderBase<ILoggingEvent> {
    protected String insertPropertiesSQL;
    protected String insertExceptionSQL;
    protected String insertSQL;
    protected static final Method GET_GENERATED_KEYS_METHOD = null;
    private CustomSQLDialect dialect;
    private DBNameResolver dbNameResolver;


    public void append(ILoggingEvent eventObject) {
        Connection connection = null;
        PreparedStatement insertStatement = null;
        try {
            connection = connectionSource.getConnection();
            connection.setAutoCommit(false);
            insertStatement = connection.prepareStatement(getInsertSQL());

//            if (cnxSupportsGetGeneratedKeys) {
//                String EVENT_ID_COL_NAME = "EVENT_ID";
//                // see
//                if (connectionSource.getSQLDialectCode() == SQLDialectCode.POSTGRES_DIALECT) {
//                    EVENT_ID_COL_NAME = EVENT_ID_COL_NAME.toLowerCase();
//                }
//                insertStatement = connection.prepareStatement(getInsertSQL(), new String[] { EVENT_ID_COL_NAME });
//            } else {
//                insertStatement = connection.prepareStatement(getInsertSQL());
//            }

//            long eventId;
            // inserting an event and getting the result must be exclusive
            synchronized (this) {
                subAppend(eventObject, connection, insertStatement);
//                eventId = selectEventId(insertStatement, connection);
            }
//            secondarySubAppend(eventObject, connection, eventId);

            connection.commit();
        } catch (Throwable sqle) {
            addError("problem appending event", sqle);
        } finally {
            DBHelper.closeStatement(insertStatement);
            DBHelper.closeConnection(connection);
        }
    }
    @Override
    protected Method getGeneratedKeysMethod() {
        return null;
    }

    @Override
    protected String getInsertSQL() {
       return dialect.getInsertSQL();
    }

    public void setDialect(CustomSQLDialect dialect) {
        this.dialect = dialect;
    }

    @Override
    protected void subAppend(ILoggingEvent event, Connection connection, PreparedStatement insertStatement) throws Throwable {
        bindLoggingEventWithInsertStatement(insertStatement, event);
//        bindLoggingEventArgumentsWithPreparedStatement(insertStatement, event.getArgumentArray());

        // This is expensive... should we do it every time?
//        bindCallerDataWithPreparedStatement(insertStatement, event.getCallerData());

        int updateCount = insertStatement.executeUpdate();
        if (updateCount != 1) {
            addWarn("Failed to insert loggingEvent");
        }
    }

    void bindLoggingEventWithInsertStatement(PreparedStatement stmt, ILoggingEvent event) throws SQLException {
        Map<String, Integer> indexMap = dialect.getIndexMap();
        for(String key : indexMap.keySet()) {
            if(key.equals("LOG_DATE")) {
                Date date = new Date(event.getTimeStamp());
                stmt.setObject(indexMap.get(key), date);
            } else if(key.equals("LEVEL")) {
                stmt.setString(indexMap.get(key), event.getLevel().toString());
            } else if(key.equals("MESSAGE")) {
                stmt.setString(indexMap.get(key), event.getFormattedMessage());
            } else if(key.equals("LOGGER_NAME")) {
                stmt.setString(indexMap.get(key), event.getLoggerName());
            } else if(key.equals("EXCEPTION")) {
                String exceptionMessage = null;
                if (event.getThrowableProxy() != null) {
                    exceptionMessage = event.getThrowableProxy().getMessage();
                }
                stmt.setString(indexMap.get(key), exceptionMessage);
            }
        }
    }

//    void bindLoggingEventArgumentsWithPreparedStatement(PreparedStatement stmt, Object[] argArray) throws SQLException {
//
//        int arrayLen = argArray != null ? argArray.length : 0;
//
//        for (int i = 0; i < arrayLen && i < 4; i++) {
//            stmt.setString(ARG0_INDEX + i, asStringTruncatedTo254(argArray[i]));
//        }
//        if (arrayLen < 4) {
//            for (int i = arrayLen; i < 4; i++) {
//                stmt.setString(ARG0_INDEX + i, null);
//            }
//        }
//    }

    @Override
    protected void secondarySubAppend(ILoggingEvent eventObject, Connection connection, long eventId) throws Throwable {

    }
}
