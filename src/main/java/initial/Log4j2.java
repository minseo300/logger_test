package initial;

import initial.Loggers;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import java.util.Map;

public class Log4j2 extends Loggers {

    private Logger logger;

    public Log4j2(String loggerName,String level, String appenderRef){
        configure(loggerName,level, appenderRef);
    }

    public void configure(String loggerName,String level, String appenderRef){
        LoggerContext context=(LoggerContext) LogManager.getContext(false);
        Configuration configuration= context.getConfiguration();
//        System.out.println("*configuration - appenders size: "+configuration.getAppenders().size());

        Level lev=null;
        if(level.equals("trace")) lev=Level.TRACE;
        else if(level.equals("info")) lev=Level.INFO;
        AppenderRef ref=AppenderRef.createAppenderRef(appenderRef, lev,null); // appenderName, level, filter
//        System.out.println("[initial.Log4j2] - (configure) - ref level: "+ref.getLevel());
        AppenderRef[] refs=new AppenderRef[]{ref};
        ConsoleAppender.Builder builder=ConsoleAppender.newBuilder();
        builder.setLayout(null);
        builder.setName(appenderRef);
//        String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger{36} - %msg%n"; // your pattern here
        String pattern = "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"; // your pattern here

        PatternLayout pl = PatternLayout.newBuilder().withPattern(pattern).build();
        builder.setLayout(pl);
        ConsoleAppender appender=builder.build();
        appender.start();
        configuration.addAppender(appender);

        LoggerConfig loggerConfig=LoggerConfig.createLogger(false, lev,loggerName,"true",refs,null,configuration,null);

        loggerConfig.addAppender(appender,lev,null);
        configuration.addLogger(loggerName,loggerConfig);
        Map<String, Appender> appenderMap=configuration.getAppenders();
//        for( String strKey : appenderMap.keySet() ){
//            Appender app = appenderMap.get(strKey);
//            System.out.println( strKey +":"+ app.getName() );
//        }
//        System.out.println("=========================================");
        context.updateLoggers();
        configuration.start();
//        System.out.println("configuration - loggers size: "+configuration.getLoggers().size());
//        System.out.println("configuration - appenders size: "+configuration.getAppenders().size());
        logger= (Logger) LogManager.getLogger(loggerName);

    }

    public Logger getLogger(String loggerName){
        return (Logger) LogManager.getLogger(loggerName);
    }
    public void info(String msg){
//        System.out.println("[initial.Log4j2] - info - msg: "+msg);
        logger.info(msg);
    }
    public void trace(String msg){ logger.trace(msg);}
    public void debug(String msg){logger.debug(msg);}
}
