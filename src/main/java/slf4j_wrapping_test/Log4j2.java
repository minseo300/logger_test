package slf4j_wrapping_test;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Marker;

import java.util.Map;

//org.slf4j.Logger
public class Log4j2 implements Mlf4j {

    private Logger logger;

    public Log4j2(String loggerName,String level, String appenderRef){
        if(appenderRef.isEmpty()) configureDefault(loggerName,level);
        else configure(loggerName,level, appenderRef);
    }

    public void configureDefault(String loggerName, String level){
        LoggerContext context=(LoggerContext) LogManager.getContext(false);

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
//        String pattern = "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"; // your pattern here
        String pattern = "%style{%d{yyyy/MM/dd HH:mm:ss,SSS}}{cyan} %highlight{[%-5p]}{FATAL=bg_red, ERROR=red,\n" +
                "            INFO=green, DEBUG=blue}  [%class] %style{[%t]}{yellow}- %m%n"; // your pattern here

        PatternLayout pl = PatternLayout.newBuilder().withPattern(pattern).build();
        builder.setLayout(pl);
        ConsoleAppender appender=builder.build();
        appender.start();
        configuration.addAppender(appender);
//        RollingFileAppender.Builder budd=RollingFileAppender.newBuilder();
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
        logger.info(String.valueOf(logger.getClass()));
        logger.info(msg);
    }

    @Override
    public void info(String format, Object arg) {

    }

    @Override
    public void info(String format, Object arg1, Object arg2) {

    }

    @Override
    public void info(String format, Object... arguments) {

    }

    @Override
    public void info(String msg, Throwable t) {

    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public void info(Marker marker, String msg) {

    }

    @Override
    public void info(Marker marker, String format, Object arg) {

    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {

    }

    @Override
    public void finest(String var1) {

    }

    @Override
    public void serve(String var1) {

    }

    @Override
    public void config(String var1) {

    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public void warn(String msg) {

    }

    @Override
    public void warn(String format, Object arg) {

    }

    @Override
    public void warn(String format, Object... arguments) {

    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {

    }

    @Override
    public void warn(String msg, Throwable t) {

    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    @Override
    public void warn(Marker marker, String msg) {

    }

    @Override
    public void warn(Marker marker, String format, Object arg) {

    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {

    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void error(String msg) {

    }

    @Override
    public void error(String format, Object arg) {

    }

    @Override
    public void error(String format, Object arg1, Object arg2) {

    }

    @Override
    public void error(String format, Object... arguments) {

    }

    @Override
    public void error(String msg, Throwable t) {

    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    @Override
    public void error(Marker marker, String msg) {

    }

    @Override
    public void error(Marker marker, String format, Object arg) {

    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {

    }

    public String getName() {
        return null;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    public void trace(String msg){ logger.trace(msg);}

    @Override
    public void trace(String format, Object arg) {

    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {

    }

    @Override
    public void trace(String format, Object... arguments) {

    }

    @Override
    public void trace(String msg, Throwable t) {

    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public void trace(Marker marker, String msg) {

    }

    @Override
    public void trace(Marker marker, String format, Object arg) {

    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {

    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {

    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    public void debug(String msg){logger.debug(msg);}

    @Override
    public void debug(String format, Object arg) {

    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {

    }

    @Override
    public void debug(String format, Object... arguments) {

    }

    @Override
    public void debug(String msg, Throwable t) {

    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    @Override
    public void debug(Marker marker, String msg) {

    }

    @Override
    public void debug(Marker marker, String format, Object arg) {

    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {

    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }
}
