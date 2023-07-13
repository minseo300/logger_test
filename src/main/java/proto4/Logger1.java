package proto4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


// ApplicationLogger
public class Logger1  extends MyLogger {




    public static Logger1 getLogger(){
        return (Logger1) MyLoggerManager.getMyLoggerManager().getLogger(APPLICATION_LOGGER_NAME);
    }
    public Logger1(Mlf4j logger, String loggerName) {
        super(logger, loggerName,null);
    }

    private static final String APPLICATION_LOGGER_NAME = new StringBuilder().append("Example").append(".").append(APPLICATION_NAME).toString();



    public static void initialize() throws IOException {
        Loggers logger1=MyLoggerManager.getMyLoggerManager().getMyLoggerType().getLoggersList().get(0);
        String frameworkType=MyLoggerManager.getMyLoggerManager().getFrameworkType();
        Logger1 logger=new Logger1(configure(APPLICATION_NAME,APPLICATION_LOGGER_NAME,"defaultLogger1Appender",true,logger1),APPLICATION_LOGGER_NAME);

//        if(frameworkType.equals("log4j2")){
//            logger = new Logger1(configureLog4j2(APPLICATION_NAME,APPLICATION_LOGGER_NAME,"defaultLogger1Appender",true,logger1), APPLICATION_LOGGER_NAME);
//
//        }
//        else if(frameworkType.equals("logback")){
//            logger=new Logger1(configureLogback(APPLICATION_NAME,APPLICATION_LOGGER_NAME,"defaultLogger1Appender",true,logger1), APPLICATION_LOGGER_NAME);
//        }

        MyLoggerManager manager= MyLoggerManager.getMyLoggerManager();
        manager.addLogger(APPLICATION_LOGGER_NAME,logger);
    }


}
