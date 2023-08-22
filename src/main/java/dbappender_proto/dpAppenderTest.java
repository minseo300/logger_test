package dbappender_proto;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class dpAppenderTest {
    public static void main(String[] args) {
        System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "OFF");
        Logger logger = (Logger) LoggerFactory.getLogger("dbTest");
        logger.error("HO");
//
//
//
//        String loggingFramework = "logback";
//        LoggerConfigInfo loggerConfigInfo = new LoggerConfigInfo(true, true, true, true, true, true, "db","logging3");
//        LogbackFactory logbackFactory = new LogbackFactory();
//        Log4j2Factory log4j2Factory = new Log4j2Factory();
//        if (loggingFramework.equals("log4j2")) {
//
//        } else {
//            logbackFactory.configDBAppender();
//        }
    }
}
