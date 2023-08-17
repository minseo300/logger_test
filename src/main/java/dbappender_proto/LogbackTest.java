//package dbappender_proto;
//
//import ch.qos.logback.classic.Logger;
//import org.apache.logging.log4j.core.config.LoggerConfig;
//import org.slf4j.LoggerFactory;
//
//public class LogbackTest {
//    public static void main(String[] args) {
//
//        System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
//        System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "OFF");
////        Logger logger = (Logger) LoggerFactory.getLogger("dbTest");
////        logger.error("HO");
//
//
//
////        DBInfo dbInfo = new DBInfo("jdbc:mysql://localhost:3306/for_log_test", "root", "1234", "com.mysql.cj.jdbc.Driver");
//        String loggingFramework = "logback";
//        LoggerConfigInfo loggerConfigInfo = new LoggerConfigInfo(true, true, true, true, true, true, "db", "logging");
//        LogbackFactory logbackFactory = new LogbackFactory();
//        Log4j2Factory log4j2Factory = new Log4j2Factory();
//        if (loggingFramework.equals("log4j2")) {
//
//        } else {
//            logbackFactory.configDBAppender();
//        }
//    }
//}
