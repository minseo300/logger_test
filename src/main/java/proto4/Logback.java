package proto4;


import ch.qos.logback.classic.*;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
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

import java.lang.reflect.Method;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

// org.slf4j.Logger
public class Logback implements Mlf4j {
    private Logger logger;
    public Logback(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params,String formatter){
        if(params.getAppender().isEmpty()) configureDefault(fileName, loggerName, appenderName,additivity,params,formatter);
        else if(params.getAppender().equals("console")) configureConsole(fileName, loggerName, appenderName,additivity,params);
    }

    /**
     * Configuring Default Appender; RollingFileAppender
     * @param fileName
     * @param loggerName
     * @param appenderName
     * @param additivity
     * @param params
     */
    public void configureDefault(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params,String formatter){
        String path = params.getPath();
        String timeBasePolicyUnit=params.getTimeBasePolicyUnit();
        String rollingPolicy=params.getRollingPolicy();
        String rotatedFileName = path + fileName + MyLogger.getRotateFileNamePattern(timeBasePolicyUnit, rollingPolicy)
                + ".log";
        String timeBasePolicyValue=params.getTimeBasePolicyValue();
        String deleteRollingFilePeriod=params.getDeleteRollingFilePeriod();
        String sizeBasePolicyValue=params.getSizeBasePolicyValue();
        String limitRollingFileNumber=params.getLimitRollingFileNumber();
        Boolean async=params.getAsync();
        Level level= Level.valueOf(params.getLevel());

        // default layout
//        String layoutPattern = "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"; // your pattern here

        // setting Appender
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();

        // create custom converter to print guid
//        Map<String, String> ruleRegistry = (Map) context.getObject(CoreConstants.PATTERN_RULE_REGISTRY);
//        if (ruleRegistry == null) {
//            ruleRegistry = new HashMap<String, String>();
//        }
//        context.putObject(CoreConstants.PATTERN_RULE_REGISTRY, ruleRegistry);
//        String conversionWord = "guid";
//        String converterClass = "proto4.LogbackConversion";
//        ruleRegistry.put(conversionWord, converterClass);
//
//        PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
//        String layoutPattern = "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} %-6guid [%t] %c{1} - %msg%n"; // your pattern here.
//        logEncoder.setPattern(layoutPattern);

        LayoutWrappingEncoder logEncoder=new LayoutWrappingEncoder();
        LogbackGuidPatternLayout layout=new LogbackGuidPatternLayout();

        if(!formatter.isEmpty())  {
            String test = "proto4."+formatter;
            System.out.println("formatter exist");
            try {
                Class<?> testClass = Class.forName(test);
                Object newObj = testClass.newInstance();
                Method method = testClass.getMethod("setting");
                method.invoke(newObj);
//                LogbackTestMessageFormatter newObj=new LogbackTestMessageFormatter();
                logEncoder.setLayout(layout);
            } catch (Exception e) {

            }
        }
        else{
            logEncoder.setLayout(layout);
        }
        logEncoder.setContext(context);
        logEncoder.start();



        RollingFileAppender logFileAppender=null;
        if(rollingPolicy.equals("time")){
            // timeBasePolicyValue, timeBasePolicyUnit
            TimeBasedRollingPolicy timeBasedRollingPolicy=new TimeBasedRollingPolicy();
            if(!deleteRollingFilePeriod.isEmpty()){
                // deleteRollingFilePeriod - setMaxHistory
                logFileAppender=new TimeRollingFileAppender(timeBasePolicyValue,timeBasePolicyUnit,path,fileName);
                timeBasedRollingPolicy.setMaxHistory(MyLogger.getLogbackMaxHistory(deleteRollingFilePeriod));
            }
            else{
                // limitRollingFileNumber
                logFileAppender=new TimeRollingFileAppender(timeBasePolicyValue,timeBasePolicyUnit,limitRollingFileNumber,path,fileName);
            }
            timeBasedRollingPolicy.setContext(context);
            timeBasedRollingPolicy.setParent(logFileAppender);
            timeBasedRollingPolicy.setFileNamePattern(rotatedFileName);
            timeBasedRollingPolicy.start();

            logFileAppender.setRollingPolicy(timeBasedRollingPolicy);
        }
        else if(rollingPolicy.equals("size")){
            // sizeBasePolicyValue
            SizeBasedRollingPolicy sizeBasedRollingPolicy=new SizeBasedRollingPolicy();
            SizeBasedTriggeringPolicy sizeBasedTriggeringPolicy=new SizeBasedTriggeringPolicy();
            if(!deleteRollingFilePeriod.isEmpty()){
                // to keep alive created log files while deleteRollingFilePeriod - setMaxHistory
                logFileAppender=new SizeRollingFileAppender(path,fileName,deleteRollingFilePeriod);
                sizeBasedRollingPolicy.setDeleteRollingFilePeriod(deleteRollingFilePeriod);

            }
            else{
                // limitRollingFileNumber
                logFileAppender=new SizeRollingFileAppender(path,fileName);
                sizeBasedRollingPolicy.setMaxIndex(Integer.parseInt(limitRollingFileNumber));
            }

            logFileAppender.setFile(path+fileName+".log");
            logFileAppender.setContext(context);

            sizeBasedRollingPolicy.setContext(context);
            sizeBasedRollingPolicy.setMinIndex(1);
            sizeBasedRollingPolicy.setParent(logFileAppender);
            sizeBasedRollingPolicy.setFileNamePattern(rotatedFileName);
            sizeBasedRollingPolicy.start();

            sizeBasedTriggeringPolicy.setContext(context);
            sizeBasedTriggeringPolicy.setMaxFileSize(FileSize.valueOf(sizeBasePolicyValue));
            sizeBasedTriggeringPolicy.start();

            logFileAppender.setRollingPolicy(sizeBasedRollingPolicy);
            logFileAppender.setTriggeringPolicy(sizeBasedTriggeringPolicy);

            logFileAppender.start();

        }

        if(async){
            AsyncAppender asyncAppender=new AsyncAppender();
            asyncAppender.setContext(context);
            asyncAppender.addAppender(logFileAppender);
            asyncAppender.setQueueSize(100);
            asyncAppender.start();
        }

//        logFileAppender.rollover();
        logFileAppender.setContext(context);
        logFileAppender.setName(appenderName);
        logFileAppender.setEncoder(logEncoder);
        logFileAppender.setAppend(true);
        logFileAppender.setFile(path+fileName+".log");
        logFileAppender.start();


        this.logger=(Logger) LoggerFactory.getLogger(loggerName);
        this.logger.setAdditive(additivity);
        this.logger.setLevel(level);
        this.logger.addAppender(logFileAppender);
//        this.logger.addAppender(customRollingFileAppender);


    }
    public void configureConsole(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params){

    }

    public void info(String msg){
        this.logger.info(String.valueOf(logger.getClass()));
        this.logger.info(String.valueOf(logger.getAppender("defaultLogger1Appender").getClass()));
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
