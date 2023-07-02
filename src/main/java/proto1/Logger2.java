package proto1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// ServiceLogger or ...
public class Logger2 extends MyLogger {

    public static Logger2 getLogger(){
        return (Logger2) MyLoggerManager.getMyLoggerManager().getLogger("initial.Logger2");
    }

//    public initial.Logger2(initial.Log4j2 log4j2, String loggerName, initial.MyLogger parent) {
//        super(log4j2, loggerName,parent);
//    }
//    public initial.Logger2(initial.Logback logback, String loggerName, initial.MyLogger parent){
//        super(logback,loggerName, parent);
//    }

    public Logger2(Mlf4j logger, String loggerName, MyLogger parent) {
        super(logger, loggerName,null);
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
                logger=new Logger2(configureLog4j2(logger2,logger2[0]),logger2[0],null);
            }
            else if(configureMode.equals("default")) logger=new Logger2(configureDefaultLog4j2(logger2,logger2[0]),logger2[0],null);
        }
        else if(frameworkType.equals("logback")){
            if(configureMode.equals("file")) {
                logger=new Logger2(configureLogback(logger2,"logger2"),"logger2",null);
            }
            else if(configureMode.equals("default")) configureDefaultLogback();
        }

        MyLoggerManager manager= MyLoggerManager.getMyLoggerManager();
        manager.addLogger(logger2[0],logger);
    }
}
