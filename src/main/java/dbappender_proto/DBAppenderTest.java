package dbappender_proto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class DBAppenderTest {
    public static void main(String[] args) {


        LoggerConfigInfo loggerConfigInfo = new LoggerConfigInfo(true, true, true, true, true, true, "db","logging3");
        LogbackFactory logbackFactory = new LogbackFactory();
        Log4j2Factory log4j2Factory = new Log4j2Factory();
        DBManager loggerManager = new DBManager(loggerConfigInfo);
        loggerManager.createConnectionPool();
        loggerManager.checkTableExist();

        String loggingFramework = "logback";

        if (loggingFramework.equals("log4j2")) {
            log4j2Factory.createLogger(loggerConfigInfo);
            for(int i=0; i<100 ; i++) {
                log4j2Factory.info("TEST"+i);
            }
        } else {
            logbackFactory.createLogger(loggerConfigInfo,loggerManager.getDialect());
            for(int i=0 ; i<100 ; i++) {
                logbackFactory.logger.info("logbackTEST {}",i);
            }
        }
    }
}
