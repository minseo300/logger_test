package dbappender_proto;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.db.DataSourceConnectionSource;
import dbappender_proto.sqlDialect.CustomSQLDialect;
import org.slf4j.LoggerFactory;

public class LogbackFactory {
    public Logger logger;
    private LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    public void createLogger(LoggerConfigInfo info, CustomSQLDialect dialect) {
        LogbackDBAppender appender = configDBAppender(info, dialect);
        logger = (Logger) LoggerFactory.getLogger("dbTester_logback");
        logger.setAdditive(false);
        logger.setLevel(Level.valueOf("info"));
        logger.addAppender(appender);
    }
    private LogbackDBAppender configDBAppender(LoggerConfigInfo info, CustomSQLDialect dialect) {
        LogbackDBAppender appender = new LogbackDBAppender();
        DataSourceConnectionSource connectionSource = new DataSourceConnectionSource();
        connectionSource.setDataSource(LogConnectionPool.ds);
        connectionSource.start();
        appender.setName(info.getAppenderName());
        appender.setDialect(dialect);
        appender.setConnectionSource(connectionSource);
        appender.start();

        return appender;
    }
}
