package proto4;

import org.apache.logging.log4j.Level;

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
        Logback logger=new Logback(fileName, loggerName, appenderName,additivity,params);
        return logger;

    }
    public void info(String msg){
//        System.out.println("myLogger msg: "+msg);
        logger.info(msg);
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
    public static int getLogbackMaxHistory (String deleteRollingFilePeriod) {
        int maxHistory=0;
        String timeUnit=deleteRollingFilePeriod.substring(deleteRollingFilePeriod.length()-1);
        String period=deleteRollingFilePeriod.substring(0,deleteRollingFilePeriod.length()-1);

        switch (timeUnit) {
            case "d":
                maxHistory=60*60*24*Integer.parseInt(period);
                break;
            case "h":
                maxHistory=60*60*Integer.parseInt(period);
                break;
            case "m":
                maxHistory=60*Integer.parseInt(period);
                break;
            case "s":
                maxHistory=Integer.parseInt(period);
                break;
            default:
                maxHistory=Integer.parseInt(period);
        }
        return maxHistory;
    }

}
