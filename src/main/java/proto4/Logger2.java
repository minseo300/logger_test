//package proto4;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
//
//// ApplicationLogger
//public class Logger2  extends MyLogger {
//
//
//
//
//    public static Logger2 getLogger(){
//        return (Logger2) MyLoggerManager.getMyLoggerManager().getLogger(APPLICATION_LOGGER_NAME);
//    }
//    public Logger2(Mlf4j logger, String loggerName) {
//        super(logger, loggerName,null);
//    }
//
//    private static final String APPLICATION_LOGGER_NAME = new StringBuilder().append("Example").append(".").append(APPLICATION_NAME).toString();
//
//
//
//    public static void initialize() throws IOException {
//        Loggers logger2=MyLoggerManager.getMyLoggerManager().getMyLoggerType().getLoggersList().get(1);
//        String frameworkType=MyLoggerManager.getMyLoggerManager().getFrameworkType();
//        Logger2 logger=new Logger2(configure(APPLICATION_NAME,APPLICATION_LOGGER_NAME,"defaultLogger1Appender",true,logger2),APPLICATION_LOGGER_NAME);
//
////        if(frameworkType.equals("log4j2")){
////            logger = new Logger1(configureLog4j2(APPLICATION_NAME,APPLICATION_LOGGER_NAME,"defaultLogger1Appender",true,logger1), APPLICATION_LOGGER_NAME);
////
////        }
////        else if(frameworkType.equals("logback")){
////            logger=new Logger1(configureLogback(APPLICATION_NAME,APPLICATION_LOGGER_NAME,"defaultLogger1Appender",true,logger1), APPLICATION_LOGGER_NAME);
////        }
//
//        MyLoggerManager manager= MyLoggerManager.getMyLoggerManager();
//        manager.addLogger(APPLICATION_LOGGER_NAME,logger);
//    }
//
//
//}
