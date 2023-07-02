//package proto3;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
//
//// ApplicationLogger
//public class Logger1 extends MyLogger {
//
//    public static Logger1 getLogger(){
//        return (Logger1) MyLoggerManager.getMyLoggerManager().getLogger("Logger1");
//    }
//    public Logger1(Mlf4j logger, String loggerName) {
//        super(logger, loggerName,null);
//    }
//
//    private static final String APPLICATION_LOGGER_NAME = new StringBuilder().append("Example").append(".").append(APPLICATION_NAME).toString();
//
//
//    public static void initialize(String frameworkType) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader("/Users/iminseo/Desktop/JAVA/logger_test/src/main/resources/logger.txt"));
//        String str;
//        String[] logger1=reader.readLine().split(","); // logger1 configuration
//        String[] logger2=reader.readLine().split(","); // logger2 configuration
//        reader.close();
//        Logger1 logger=null;
//
//        if(frameworkType.equals("log4j2")){
//            logger = new Logger1(configureDefaultLog4j2(APPLICATION_NAME,APPLICATION_LOGGER_NAME,"defaultLogger1Appender",true,logger1,"default"), "logger1");
//        }
//        else if(frameworkType.equals("logback")){
//            logger=new Logger1(configureLogback(logger1,logger1[0]),logger1[0]);
//        }
//
//        MyLoggerManager manager= MyLoggerManager.getMyLoggerManager();
//        manager.addLogger(logger1[0],logger);
//    }
//}
