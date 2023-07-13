package proto4;

import java.io.IOException;


// ProObjectLogger

public class MyLogger {
    private String frameworkType;

    private String loggerName;
    private MyLogger parent;

    private Mlf4j logger;
    private static MyLoggerManager manager= MyLoggerManager.getMyLoggerManager();
    protected static String APPLICATION_NAME = "MyLoggerApp";

    public MyLogger(Mlf4j logger, String loggerName, MyLogger parent) {
        this.logger = logger;
        this.frameworkType=manager.getFrameworkType();
        this.parent=parent;
        this.loggerName=loggerName;
    }

    public static Log4j2 configureLog4j2(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params) throws IOException {

        Log4j2 logger=new Log4j2(fileName, loggerName, appenderName,additivity,params);

        return logger;
    }
    public static Logback configureLogback(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params){
//        Logback logger=new Logback(fileName, loggerName, appenderName,additivity,params,"LogbackTestMessageFormatter");
        Logback logger=new Logback(fileName, loggerName, appenderName,additivity,params,"LogbackTestMessageFormatter2");

        return logger;

    }

    public static Mlf4j configure(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params) throws IOException {
        Mlf4j ILogger=null;
        if(params.getFramework().equals("log4j2")) ILogger=configureLog4j2(fileName, loggerName, appenderName,additivity,params);
        else ILogger=configureLogback(fileName, loggerName, appenderName,additivity,params);

        return ILogger;
    }
    public void info(String msg, Object... args){
//        System.out.println("myLogger msg: "+msg);
        logger.info(msg,args);


    }

    public static String getRotateFileNamePattern (String timeUnit,
                                                   String rollingPolicy) {
        String pattern;
        if (rollingPolicy.equals("size")) {
            pattern = "-%i";
        } else {
            switch (timeUnit) {
                case "day":
                    pattern = "_%d{yyMMdd}";
                    break;
                case "hour":
                    pattern = "_%d{yyMMdd_HH}";
                    break;
                case "minute":
                    pattern = "_%d{yyMMdd_HHmm}";
                    break;
                case "second":
                    pattern = "_%d{yyMMdd_HHmmss}";
                    break;
                default:
                    pattern = "_%d{yyMMdd}";
            }
        }
        return pattern;
    }

    // convert deletePeriod string to time int
    public static int getMillisByTimeValueAndUnit (String timeUnit, String timeValue) {
        int millis=0;

        switch (timeUnit) {
            case "d":
            case "day":
            case "D":
                millis=60*60*24*Integer.parseInt(timeValue);
                break;
            case "h":
                millis=60*60*Integer.parseInt(timeValue);
                break;
            case "m":
                millis=60*Integer.parseInt(timeValue);
                break;
            case "s":
                millis=Integer.parseInt(timeValue);
                break;
            default:
                millis=Integer.parseInt(timeValue);
        }
        millis*=1000;

        return millis;
    }

}
