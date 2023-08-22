package dbappender_proto2;

import ch.qos.logback.classic.db.names.DBNameResolver;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.db.DBAppenderBase;
import ch.qos.logback.core.db.DBHelper;
import dbappender_proto2.column_converter.LogbackColumnFactory;
import dbappender_proto2.sqlDialect.CustomSQLDialect;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class LogbackDBAppender extends DBAppenderBase<ILoggingEvent> {
    protected String insertPropertiesSQL;
    protected String insertExceptionSQL;
    protected String insertSQL;
    protected static final Method GET_GENERATED_KEYS_METHOD = null;
    private CustomSQLDialect dialect;
    private DBNameResolver dbNameResolver;
    private LogbackColumnFactory logbackColumnFactory;
    private String tableName;
    private SimpleDateFormat sdf = null;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setLogbackColumnFactory(LogbackColumnFactory logbackColumnFactory) {
        this.logbackColumnFactory = logbackColumnFactory;
    }
    @Override
    protected void secondarySubAppend(ILoggingEvent eventObject, Connection connection, long eventId) throws Throwable {

    }

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
        return dialect.getInsertSQL(tableName);
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
        Map<String, String> columnPatternList = logbackColumnFactory.getColumnPatternMap(tableName);
        Map<String, Integer> patternIndexList = logbackColumnFactory.getPatternIndexMap(tableName);
        for (String columnName : columnPatternList.keySet()) {
            String pattern = columnPatternList.get(columnName);
            int index = patternIndexList.get(pattern);
            if (pattern.startsWith("d:")) {
                String dateFormat = pattern.substring(2);
                long eventTime = event.getTimeStamp();
                Date date = new Date(eventTime);
                sdf = new SimpleDateFormat(dateFormat);
                String time = sdf.format(date);
                stmt.setObject(index, time);
            } else if (pattern.startsWith("class")) {
                stmt.setObject(index, event.getCallerData()[0].getClassName());
            } else if (pattern.startsWith("contextName")) {
                stmt.setObject(index, event.getLoggerContextVO().getName());
            } else if (pattern.startsWith("file")) {
                stmt.setObject(index, event.getCallerData()[0].getFileName());
            } else if (pattern.startsWith("caller")) {
                stmt.setObject(index, event.getCallerData());
            } else if (pattern.startsWith("line")) {
                stmt.setObject(index, event.getCallerData()[0].getLineNumber());
            } else if (pattern.startsWith("m")) {
                String msg = event.getFormattedMessage();
                stmt.setObject(index, event.getFormattedMessage());
            } else if (pattern.startsWith("level")) {
                String level = String.valueOf(event.getLevel());
                stmt.setObject(index, level);
            } else if (pattern.startsWith("thread")) {
                stmt.setObject(index, event.getThreadName());
            } else if (pattern.startsWith("ex")) {
                stmt.setObject(index, event.getThrowableProxy().getMessage());
            } else if (pattern.startsWith("logger")) {
                stmt.setObject(index, event.getLoggerName());
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
}
