//package proto2;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
//// ServiceLogger or ...
//public class Logger2 extends MyLogger {
//    private static final String SERVICE_GROUP_LOGGER_NAME_PREFIX = new StringBuilder().append("Example").append(".").append(APPLICATION_NAME).append(".")
//            .toString();
//
//
//    public static Logger2 getLogger() {
//        return (Logger2) MyLoggerManager.getMyLoggerManager().getLogger("Logger2");
//    }
//    public Logger2(Mlf4j logger, String loggerName, MyLogger parent) {
//        super(logger, loggerName,parent);
//    }
//
//    public static void initialize(String frameworkType, String configureMode) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader("/Users/iminseo/Desktop/JAVA/logger_test/src/main/resources/logger.txt"));
//        String str;
//        String[] logger1=reader.readLine().split(",");
//        String[] logger2=reader.readLine().split(",");
//        reader.close();
//        String serviceGroupName="serviceGroup1";
//        String loggerName = SERVICE_GROUP_LOGGER_NAME_PREFIX + serviceGroupName;
//
//        Logger2 logger=null;
//        MyLogger parent=Logger1.getLogger();
//        if(frameworkType.equals("log4j2")){
//            if(configureMode.equals("file")) {
//                logger=new Logger2(configureLog4j2(logger2,logger2[0]),logger2[0],null);
//            }
//            else if(configureMode.equals("default")){
//                logger=new Logger2(configureDefaultLog4j2(serviceGroupName,loggerName,"defaultLogger2Appender",true,logger2),logger2[0],parent);
//            }
//        }
//        else if(frameworkType.equals("logback")){
//            if(configureMode.equals("file")) {
//                logger=new Logger2(configureLogback(logger2,"logger2"),"logger2",null);
//            }
//            else if(configureMode.equals("default")) configureDefaultLogback();
//        }
//
//        MyLoggerManager manager= MyLoggerManager.getMyLoggerManager();
//        manager.addLogger(logger2[0],logger);
//    }
//}
