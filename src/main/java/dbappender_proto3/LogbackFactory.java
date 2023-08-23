package dbappender_proto3;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.db.DataSourceConnectionSource;
import dbappender_proto3.column_converter.LogbackColumnFactory;
import org.slf4j.LoggerFactory;

public class LogbackFactory {
    private Info info = Info.getInstance();
    public Logger logger;
    public AppenderManager am = AppenderManager.getInstance();

    private static LogbackFactory logbackFactory = new LogbackFactory();
    public static LogbackFactory getInstance() {
        return logbackFactory;
    }

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
        String loggerName = "logback db tester";
        String loggerLevel = "info";

        logger = (Logger) LoggerFactory.getLogger(loggerName);
        logger.setAdditive(false);
        logger.setLevel(Level.valueOf(loggerLevel));
        logger.addAppender(appender);

    }
}
