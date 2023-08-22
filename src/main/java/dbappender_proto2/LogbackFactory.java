package dbappender_proto2;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.db.DataSourceConnectionSource;
import dbappender_proto2.column_converter.LogbackColumnFactory;
import dbappender_proto2.sqlDialect.CustomSQLDialect;
import org.slf4j.LoggerFactory;

public class LogbackFactory {
    private Info info = Info.getInstance();
    public Logger logger;
    public AppenderManager am = AppenderManager.getInstance();

//    public LogbackColumnFactory logbackColumnFactory = new LogbackColumnFactory();

    public LogbackDBAppender createDBAppender(LogbackColumnFactory columnFactory) {
        DataSourceConnectionSource connectionSource = new DataSourceConnectionSource();
        connectionSource.setDataSource(ConnectionPool.ds);
        connectionSource.setContext((LoggerContext)LoggerFactory.getILoggerFactory());
        connectionSource.start();

        LogbackDBAppender appender = new LogbackDBAppender();
        appender.setName(info.appenderName);
        appender.setConnectionSource(connectionSource);
        appender.setContext((LoggerContext)LoggerFactory.getILoggerFactory());
        appender.setDialect(info.sqlDialect);
        appender.setTableName(info.tableName);
        appender.setLogbackColumnFactory(columnFactory);
        appender.start();

        return appender;
    }

    public void createLogger(LogbackDBAppender appender) {
        String loggerName = "logback DB Tester";
        String loggerLevel = "info";

        logger = (Logger) LoggerFactory.getLogger(loggerName);
        logger.setAdditive(false);
        logger.setLevel(Level.valueOf(loggerLevel));
        logger.addAppender(appender);
    }
}
