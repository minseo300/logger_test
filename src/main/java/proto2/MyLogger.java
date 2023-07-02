//package proto2;
//
//import org.apache.logging.log4j.Level;
//import org.apache.logging.log4j.core.LoggerContext;
//import org.apache.logging.log4j.core.appender.RollingFileAppender;
//import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
//import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
//import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
//import org.apache.logging.log4j.core.appender.rolling.action.*;
//import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
//import org.apache.logging.log4j.core.config.AppenderRef;
//import org.apache.logging.log4j.core.config.Configuration;
//import org.apache.logging.log4j.core.config.LoggerConfig;
//import org.apache.logging.log4j.core.layout.PatternLayout;
//
//import java.io.IOException;
//import java.util.zip.Deflater;
//
//
//// ProObjectLogger
//
//public class MyLogger {
//    private String frameworkType;
//
//    private String loggerName;
//    private MyLogger parent;
////    private initial.Log4j2 log4j2;
////    private initial.Logback logback;
//    private Mlf4j logger;
//    private static MyLoggerManager manager= MyLoggerManager.getMyLoggerManager();
//
//    protected static String APPLICATION_NAME = "MyLoggerApp";
//
//    public MyLogger(Mlf4j logger, String loggerName, MyLogger parent) {
//        this.logger = logger;
//        this.frameworkType=manager.getFrameworkType();
//        this.parent=parent;
//        this.loggerName=loggerName;
//    }
//
//
//    public static Log4j2 configureLog4j2(String[] params, String loggerName) throws IOException {
//        String level=params[1];
//        String appenderRef=params[2];
////        System.out.println("level: "+level+" appenderRef: "+appenderRef);
//        Log4j2 logger=new Log4j2(loggerName,level, appenderRef);
//
//        return logger;
////        manager.addLogger(loggerName,new initial.MyLogger(loggerName,));
//    }
//    public static Log4j2 configureDefaultLog4j2(String fileName,String loggerName, String appenderName, Boolean additivity,String[] params,String configureMode){
//
//
//        Level level= Level.valueOf(params[1]);
//        String async=null, rollingPolicy=null, limitRollingFileNumber=null,deleteRollingFilePeriod=null, logDebugEnable=null, formatter=null,
//                timeBasePolicyUnit=null, timeBasePolicyValue=null;
//        async=params[2];
//        rollingPolicy=params[3];
//        if(rollingPolicy.equals("size")) {
//            limitRollingFileNumber=params[4];
//            deleteRollingFilePeriod=params[5];
//            logDebugEnable=params[6];
//        }
//        else if(rollingPolicy.equals("time")) {
//            timeBasePolicyUnit=params[4];
//            timeBasePolicyValue=params[5];
//            deleteRollingFilePeriod=params[6];
//        }
//        formatter=params[7];
//
//        Log4j2 logger=new Log4j2(loggerName, level,configureMode);
//        return logger;
//    }
//    public static Logback configureLogback(String[] params, String loggerName){
//        String level=params[1];
//        String appenderRef=params[2];
//        Logback logger=new Logback(loggerName);
//        return logger;
//
//    }
//    public static void configureDefaultLogback(){
//
//    }
//
//    public void info(String msg){
////        System.out.println("myLogger msg: "+msg);
//        logger.info(msg);
//    }
//    public void fatal(String msg){
//        logger.fatal(msg);
//    }
//
//    public static String getRotateFileNamePattern (String timeUnit,
//                                                      String rollingPolicy) {
//        String pattern;
//
//        if (rollingPolicy.equals("size")) {
//            pattern = "-%i";
//        } else {
//            switch (timeUnit) {
//                case "day":
//                    pattern = "_%d{yyMMdd}";
//                    break;
//                case "hour":
//                    pattern = "_%d{yyMMdd_HH}";
//                    break;
//                case "minute":
//                    pattern = "_%d{yyMMdd_HHmm}";
//                    break;
//                case "second":
//                    pattern = "_%d{yyMMdd_HHmmss}";
//                    break;
//                default:
//                    pattern = "_%d{yyMMdd}";
//            }
//        }
//        return pattern;
//    }
//
//
//}
