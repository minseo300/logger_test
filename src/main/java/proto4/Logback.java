package proto4;


import ch.qos.logback.classic.*;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.joran.action.ConversionRuleAction;
import ch.qos.logback.core.pattern.ConverterUtil;
import ch.qos.logback.core.rolling.*;
import ch.qos.logback.core.util.FileSize;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import sun.rmi.runtime.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

// org.slf4j.Logger
public class Logback implements Mlf4j {
    private Logger logger;
    private String messageFormatter=null;
    public Logback(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params,String formatter){
        if(params.getAppender().isEmpty()) configureDefault(fileName, loggerName, appenderName,additivity,params,formatter);
        else if(params.getAppender().equals("console")) configureConsole(fileName, loggerName, appenderName,additivity,params);
    }
    private LogbackFactory logbackFactory=new LogbackFactory();

    /**
     * Configuring Default Appender; RollingFileAppender
     * @param fileName
     * @param loggerName
     * @param appenderName
     * @param additivity
     * @param params
     */
    public void configureDefault(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params,String formatter){
        Boolean async=params.getAsync();
        Level level= Level.valueOf(params.getLevel());

        // default layout
//        String layoutPattern = "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"; // your pattern here

        // setting Appender
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//        context.reset();

//
        if(!formatter.isEmpty())  {
            messageFormatter=formatter;
            System.out.println("messageFormatter: "+messageFormatter);
        }


        RollingFileAppender logFileAppender=logbackFactory.create(fileName,loggerName,appenderName,additivity,params,formatter,context);

        this.logger=(Logger) LoggerFactory.getLogger(loggerName);
        this.logger.setAdditive(additivity);
        this.logger.setLevel(level);

        if(async){
            AsyncAppender asyncAppender=new AsyncAppender();
//            LogbackAsyncAppender2 asyncAppender=new LogbackAsyncAppender2(this.logger.getName(),this.logger,level);
            asyncAppender.setContext(context);
            asyncAppender.addAppender(logFileAppender);
            asyncAppender.setQueueSize(100);
            asyncAppender.start();
            this.logger.addAppender(asyncAppender);
        }
        else{
            this.logger.addAppender(logFileAppender);
        }
    }
    public void configureConsole(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params){

    }
    public String convertMessageFormatNoArgs(String msg,String formatter) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String test = "proto4."+formatter;
        String formatted="";
        Class<?> testClass = Class.forName(test);
        Object newObj = testClass.newInstance();
        Method method = testClass.getMethod("withNoArgs",String.class);
        Object ret=method.invoke(newObj,msg);
        formatted= (String) ret;
        return formatted;
    }
    public String convertMessageFormatArgs(String msg,String formatter,Object... params) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String test = "proto4."+formatter;
        String formatted="";
        Class<?> testClass = Class.forName(test);
        Object newObj = testClass.newInstance();
        Method method = testClass.getDeclaredMethod("withArgs",String.class,Object.class);
        Object ret=method.invoke(newObj,msg,params);
        formatted= (String) ret;
        return formatted;
    }
    public void info(String msg){
//        if(messageFormatter!=null){
//            try {
//                msg=convertMessageFormatNoArgs(msg,messageFormatter);
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            } catch (InstantiationException e) {
//                throw new RuntimeException(e);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            } catch (NoSuchMethodException e) {
//                throw new RuntimeException(e);
//            } catch (InvocationTargetException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        this.logger.makeLoggingEventBuilder()
        this.logger.info(msg);
    }

    @Override
    public void info(String format, Object arg) {

    }

    @Override
    public void info(String format, Object arg1, Object arg2) {

    }
    @Override
    public void info(String format, Object... arguments) {
//        if(messageFormatter!=null){
//            try {
//                format=convertMessageFormatNoArgs(format,messageFormatter);
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            } catch (InstantiationException e) {
//                throw new RuntimeException(e);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            } catch (NoSuchMethodException e) {
//                throw new RuntimeException(e);
//            } catch (InvocationTargetException e) {
//                throw new RuntimeException(e);
//            }
//        }
        format=getMsg(format);
        logger.info(format,arguments);
    }

    public String getMsg(String format) {
        String guid="[GUID-132392983921849814] ";
        String stackTrace=" (proto4.Main.main.455)";
        return guid+format+stackTrace;
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

    @Override
    public void fatal(String message) {
        logger.error(message);
    }

    public String getName() {
        return null;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    public void trace(String msg){
//        logger.trace(msg);
    }

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

    public void debug(String msg){
//        logger.debug(msg);
    }

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
