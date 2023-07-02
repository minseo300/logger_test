package initial;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyLoggerManager {
    private static MyLoggerManager loggerManager=new MyLoggerManager();
    private String frameworkType;
    private Map<String,MyLogger> loggerRegistry;


    public static MyLoggerManager getMyLoggerManager(){
        return loggerManager;
    }
    public String getFrameworkType(){return this.frameworkType;}

    public void init(String frameworkType,String configureMode) throws IOException {
        this.frameworkType=frameworkType;
        this.loggerRegistry=new HashMap<>();

        Logger1.initialize(frameworkType,configureMode);
        Logger2.initialize(frameworkType,configureMode);
    }
    public void addLogger(String loggerName,MyLogger logger){
        loggerRegistry.put(loggerName,logger);
    }
    public MyLogger getLogger(String loggerName){
        return loggerRegistry.get(loggerName);
    }

    public Logger1 getLogger1Logger(){
        return (Logger1) loggerRegistry.get("logger1");
    }
    public Logger2 getLogger2Logger(String loggerName){
        return (Logger2) loggerRegistry.get(loggerName);
    }

}
