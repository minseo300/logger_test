package proto4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// ServiceGroupLogger
public class Logger2 extends MyLogger {
    private static final String SERVICE_GROUP_LOGGER_NAME_PREFIX = new StringBuilder().append("Example").append(".").append(APPLICATION_NAME).append(".")
            .toString();
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
    public static void initialize() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("/Users/iminseo/Desktop/JAVA/logger_test/src/main/resources/logger.txt"));
        String str;
        String[] logger1=reader.readLine().split(",");
        String[] logger2=reader.readLine().split(",");
        reader.close();

        Logger2 logger=null;


        MyLoggerManager manager= MyLoggerManager.getMyLoggerManager();
        manager.addLogger(logger2[0],logger);
    }
}
