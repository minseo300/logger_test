package slf4j_wrapping_test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


// SystemLogger
public class Logger1 extends MyLogger {




    public static Logger1 getLogger(){
        return (Logger1) MyLoggerManager.getMyLoggerManager().getLogger("logger1");
    }
    public Logger1(Log4j2 log4j2, String loggerName) {
        super(log4j2, loggerName,null);
    }
    public Logger1(Logback logback, String loggerName){
        super(logback,loggerName, null);
    }


    public static void initialize(String frameworkType,String configureMode) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("/Users/iminseo/Desktop/JAVA/logger_test/src/main/resources/logger.txt"));
        String str;
        String[] logger1=reader.readLine().split(","); // logger1 configuration
        String[] logger2=reader.readLine().split(","); // logger2 configuration
        reader.close();
        Logger1 logger=null;
        if(frameworkType.equals("log4j2")){
            if(configureMode.equals("file")) {
                logger=new Logger1(configureLog4j2(logger1,logger1[0]),logger1[0]);
            }
            else if(configureMode.equals("default")) {
//                logger = new initial.Logger1(configureDefaultLog4j2(), "logger1");
            }

        }
        else if(frameworkType.equals("logback")){
            if(configureMode.equals("file")) {
                logger=new Logger1(configureLogback(logger1,logger1[0]),logger1[0]);
            }
            else if(configureMode.equals("default")) configureDefaultLogback();
        }

        MyLoggerManager manager= MyLoggerManager.getMyLoggerManager();
        manager.addLogger(logger1[0],logger);
    }
}
