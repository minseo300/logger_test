//package proto4;
//
//import ch.qos.logback.core.LayoutBase;
//import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
//import org.apache.logging.log4j.message.MessageFactory;
//import org.yaml.snakeyaml.Yaml;
//import org.yaml.snakeyaml.constructor.Constructor;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.*;
//
//public class MyLoggerManager {
//    private static MyLoggerManager loggerManager=new MyLoggerManager();
//    private String frameworkType;
//    private Map<String, MyLogger> loggerRegistry;
//    private MyLoggerType myLoggerType;
//
//
//    public static MyLoggerManager getMyLoggerManager(){
//        return loggerManager;
//    }
//    public String getFrameworkType(){return this.frameworkType;}
//    public MyLoggerType getMyLoggerType() {return this.myLoggerType;}
//
//    public void init() throws IOException {
//        this.loggerRegistry=new HashMap<>();
//
//        MyLoggerType my=new MyLoggerType();
//        my.setType("logback");
//        List<Loggers> loggers=new ArrayList<>();
////        Loggers loggers1=new Loggers("log4j2","Logger1",false,"","/Users/iminseo/Desktop/logs/","","time","5", "3m",true,"minute","2","1Kb","INFO");
//        Loggers loggers1=new Loggers("logback","Logger1",true,"","/Users/iminseo/Desktop/logs/","","time","5", "10s",true,"second","5","1Kb","INFO");
//
//        Loggers loggers2=new Loggers("log4j2","Logger2",false,"TestMessageFormatter","/Users/iminseo/Desktop/logs/","","time","","3m",false,"minute","1","","INFO");
//        loggers.add(loggers1);
//        loggers.add(loggers2);
//        my.setLoggersList(loggers);
//        this.myLoggerType=my;
//        this.frameworkType= myLoggerType.getType();
//        Logger1.initialize();
////        Logger2.initialize();
//    }
//    public void addLogger(String loggerName, MyLogger logger){
//        loggerRegistry.put(loggerName,logger);
//    }
//    public MyLogger getLogger(String loggerName){
//        return loggerRegistry.get(loggerName);
//    }
//
//    public Logger1 getLogger1Logger(String loggerName){
//        return (Logger1) loggerRegistry.get(loggerName);
//    }
//    public Logger2 getLogger2Logger(String loggerName){
//        return (Logger2) loggerRegistry.get(loggerName);
//    }
//
////    public static MessageFactory getLogFormatter(String loggerName) {
////        MessageFactory factory = loggerMessageFactories.get(loggerName);
////        while (factory == null) {
////            int index = loggerName.lastIndexOf(".");
////            if (index != -1) {
////                loggerName = loggerName.substring(0, index);
////                factory = loggerMessageFactories.get(loggerName);
////            } else {
////                break;
////            }
////        }
////        return factory;
////    }
//
////    public static LayoutBase getLogbackLogFormatter(String loggerName){
////
////    }
//}
