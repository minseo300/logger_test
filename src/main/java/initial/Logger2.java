package initial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// ServiceLogger or ...
public class Logger2 extends MyLogger{

    public static Logger2 getLogger(){
        return (Logger2) MyLoggerManager.getMyLoggerManager().getLogger("logger2");
    }

    public Logger2(Log4j2 log4j2, String loggerName, MyLogger parent) {
        super(log4j2, loggerName,parent);
    }
    public Logger2(Logback logback, String loggerName, MyLogger parent){
        super(logback,loggerName, parent);
    }


    public static void initialize(String frameworkType, String configureMode) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("/Users/iminseo/Desktop/JAVA/logger_test/src/main/resources/logger.txt"));
        String str;
        String[] logger1=reader.readLine().split(",");
        String[] logger2=reader.readLine().split(",");
        reader.close();

        Logger2 logger=null;
        if(frameworkType.equals("log4j2")){
            if(configureMode.equals("file")) {
                logger=new Logger2(configureLog4j2(logger2,"logger2"),"logger2",null);
            }
            else if(configureMode.equals("default")) configureDefaultLog4j2();
        }
        else if(frameworkType.equals("logback")){
            if(configureMode.equals("file")) {
                logger=new Logger2(configureLogback(logger2,"logger2"),"logger2",null);
            }
            else if(configureMode.equals("default")) configureDefaultLogback();
        }

        MyLoggerManager manager=MyLoggerManager.getMyLoggerManager();
        manager.addLogger("logger2",logger);
    }
}
