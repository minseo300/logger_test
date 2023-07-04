package proto4;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MyLoggerManager {
    private static MyLoggerManager loggerManager=new MyLoggerManager();
    private String frameworkType;
    private Map<String, MyLogger> loggerRegistry;
    private MyLoggerType myLoggerType;


    public static MyLoggerManager getMyLoggerManager(){
        return loggerManager;
    }
    public String getFrameworkType(){return this.frameworkType;}
    public MyLoggerType getMyLoggerType() {return this.myLoggerType;}

    public void init() throws IOException {
        this.loggerRegistry=new HashMap<>();
//        InputStream inputStream = this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("my-logger.yml"); ///Users/iminseo/Desktop/JAVA/logger_test/src/main/resources/my-logger.yml
//
//        Yaml yaml = new Yaml();
//        LinkedHashMap myLoggers = yaml.load(inputStream);
////        this.myLoggerType=myLoggers;
//        MyLoggerType my=new MyLoggerType();
//        my.setType(myLoggerType.getType());
//        my.setLoggersList();
//        System.out.println(myLoggers.get("loggers"));
////        this.myLoggerType= propMap.get("loggers");

        MyLoggerType my=new MyLoggerType();
        my.setType("logback");
        List<Loggers> loggers=new ArrayList<>();
        Loggers loggers1=new Loggers("Logger1",false,"TestMessageFormatter","/Users/iminseo/Desktop/logs/","","size","3","1m",true,"second","3","1kb","INFO");
        Loggers loggers2=new Loggers("Logger2",false,"TestMessageFormatter","/Users/iminseo/Desktop/logs/","","time","","20s",false,"second","","","INFO");
        loggers.add(loggers1);
        loggers.add(loggers2);
        my.setLoggersList(loggers);
        this.myLoggerType=my;
        this.frameworkType= myLoggerType.getType();
        Logger1.initialize();
        Logger2.initialize();
    }
    public void addLogger(String loggerName, MyLogger logger){
        loggerRegistry.put(loggerName,logger);
    }
    public MyLogger getLogger(String loggerName){
        return loggerRegistry.get(loggerName);
    }

    public Logger1 getLogger1Logger(String loggerName){
        return (Logger1) loggerRegistry.get(loggerName);
    }
    public Logger2 getLogger2Logger(String loggerName){
        return (Logger2) loggerRegistry.get(loggerName);
    }

}