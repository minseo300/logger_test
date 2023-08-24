package dbappender_proto3;

import ch.qos.logback.classic.db.names.DBNameResolver;
import ch.qos.logback.classic.pattern.*;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.db.DBAppenderBase;
import ch.qos.logback.core.db.DBHelper;
import ch.qos.logback.core.pattern.Converter;
import dbappender_proto3.column_converter.LogbackColumnFactory;
import dbappender_proto3.sqlDialect.CustomSQLDialect;


import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LogbackDBAppender extends LogbackDBAppenderBase<ILoggingEvent> {
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
        List<Object> converterList = logbackColumnFactory.getConverterList(tableName);
        int index = 0;
        for (Object c : converterList) {
            c = (Converter) c;
            index = converterList.indexOf(c);
            if (c instanceof DateConverter) {
                String time = ((DateConverter) c).convert(event);
                stmt.setObject(index, time);
            } else if (c instanceof RelativeTimeConverter) {
                String rt = ((RelativeTimeConverter) c).convert(event);
                stmt.setObject(index, rt);
            } else if (c instanceof LevelConverter) {
                String level = ((LevelConverter) c).convert(event);
                stmt.setObject(index, level);
            } else if (c instanceof ThreadConverter) {
                String thread = ((ThreadConverter) c).convert(event);
                stmt.setObject(index, thread);
            } else if (c instanceof LoggerConverter) {
                String logger = ((LoggerConverter) c).convert(event);
                stmt.setObject(index, logger);
            } else if (c instanceof MessageConverter) {
                String message = ((MessageConverter) c).convert(event);
                stmt.setObject(index, message);
            } else if (c instanceof ClassOfCallerConverter) {
                String classCaller = ((ClassOfCallerConverter) c).convert(event);
                stmt.setObject(index, classCaller);
            } else if (c instanceof MethodOfCallerConverter) {
                String methodCaller = ((MethodOfCallerConverter) c).convert(event);
                stmt.setObject(index, methodCaller);
            } else if (c instanceof LineOfCallerConverter) {
                String lineCaller = ((LineOfCallerConverter) c).convert(event);
                stmt.setObject(index, lineCaller);
            } else if (c instanceof FileOfCallerConverter) {
                String fileCaller = ((FileOfCallerConverter) c).convert(event);
                stmt.setObject(index, fileCaller);
            } else if (c instanceof ThrowableProxyConverter) {
                String exception = ((ThrowableProxyConverter) c).convert(event);
                stmt.setObject(index, exception);
            } else if (c instanceof ExtendedThrowableProxyConverter) {
//                String extendedException = c
            } else if (c instanceof NopThrowableInformationConverter) {
                String nop = ((NopThrowableInformationConverter) c).convert(event);
                stmt.setObject(index, nop);
            } else if (c instanceof  ContextNameConverter) {
                String contextName = ((ContextNameConverter) c).convert(event);
                stmt.setObject(index, contextName);
            } else if (c instanceof CallerDataConverter) {
                String callerData = ((CallerDataConverter) c).convert(event);
                stmt.setObject(index, callerData);
            } else if (c instanceof PropertyConverter){
                String property = ((PropertyConverter) c).convert(event);
                stmt.setObject(index, property);
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
