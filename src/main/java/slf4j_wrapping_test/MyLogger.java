//package slf4j_wrapping_test;
//
//import java.io.IOException;
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
//    public MyLogger(Log4j2 log4j2, String loggerName, MyLogger parent){
//       this.frameworkType= manager.getFrameworkType();
//       this.loggerName=loggerName;
//       this.parent=parent;
//       this.logger=log4j2;
////       if(frameworkType.equals("log4j2")) log4j2=new initial.Log4j2(loggerName);
////       else if(frameworkType.equals("logback")) logback=new initial.Logback(loggerName);
//    }
//    public MyLogger(Logback logback, String loggerName, MyLogger parent){
//        this.frameworkType= manager.getFrameworkType();
//        this.loggerName=loggerName;
//        this.parent=parent;
//        this.logger=logback;
//    }
//    public static Log4j2 configureLog4j2(String[] params, String loggerName) throws IOException {
//        String level=params[1];
//        String appenderRef=params[2];
////        System.out.println("level: "+level+" appenderRef: "+appenderRef);
//        Log4j2 logger=new Log4j2(loggerName,level, appenderRef);
//
//        return logger;
////        manager.addLogger(loggerName,new initial.MyLogger(loggerName,));
//    }
//    public static Log4j2 configureDefaultLog4j2(String[] params, String loggerName){
//        String level=params[1];
//        String appenderRef="";
//        Log4j2 logger=new Log4j2(loggerName, level,appenderRef);
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
//    public void info(String msg){
////        System.out.println("myLogger msg: "+msg);
//        logger.info(msg);
//    }
////    public void info(String msg){
//////        System.out.println("[initial.MyLogger] - frameworkType: "+this.frameworkType);
////        if(frameworkType.equals("log4j2")){
////            log4j2.info(msg);
////        }
////        else if(frameworkType.equals("logback")){
////            logback.info(msg);
////        }
////    }
////
////    public void trace(String msg){
////        if(frameworkType.equals("log4j2")){
////            log4j2.trace(msg);
////        }
////        else if(frameworkType.equals("logback")){
////            logback.trace(msg);
////        }
////    }
////    public void debug(String msg){
////        if(frameworkType.equals("log4j2")){
////            log4j2.debug(msg);
////        }
////        else if(frameworkType.equals("logback")){
////            logback.debug(msg);
////        }
////    }
//
//}
