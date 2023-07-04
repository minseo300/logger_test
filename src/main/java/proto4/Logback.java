package proto4;


import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.*;
import ch.qos.logback.core.util.FileSize;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import sun.rmi.runtime.Log;

// org.slf4j.Logger
public class Logback implements Mlf4j {
    private Logger logger;
    public Logback(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params){
        if(params.getAppender().isEmpty()) configureDefault(fileName, loggerName, appenderName,additivity,params);
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
    public void configureDefault(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params){
        System.out.println("Logback configureDefault");
        String path = params.getPath();
        String timeBasePolicyUnit=params.getTimeBasePolicyUnit();
        String rollingPolicy=params.getRollingPolicy();
        String rotatedFileName = path + fileName + MyLogger.getRotateFileNamePattern(timeBasePolicyUnit, rollingPolicy)
                + ".log";
        System.out.println("rotatedFileName: "+rotatedFileName);
        String timeBasePolicyValue=params.getTimeBasePolicyValue();
        String deleteRollingFilePeriod=params.getDeleteRollingFilePeriod();
        String sizeBasePolicyValue=params.getSizeBasePolicyValue();
        String limitRollingFileNumber=params.getLimitRollingFileNumber();
        Boolean async=params.getAsync();
        Level level= Level.valueOf(params.getLevel());

        // default layout
        String layoutPattern = "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"; // your pattern here

        // setting Appender
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//        context.reset();
        PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
        logEncoder.setContext(context);
        logEncoder.setPattern(layoutPattern);
        logEncoder.start();


        RollingFileAppender logFileAppender = new RollingFileAppender();
//        LogbackCustomRollingFileAppender logFileAppender=new LogbackCustomRollingFileAppender(timeBasePolicyUnit,timeBasePolicyValue,);
        if(rollingPolicy.equals("time")){ // time based rolling file appender
            TimeBasedRollingPolicy logFilePolicy = new TimeBasedRollingPolicy();
            logFilePolicy.setContext(context);
            logFilePolicy.setFileNamePattern(rotatedFileName);
            logFilePolicy.setMaxHistory(MyLogger.getLogbackMaxHistory(deleteRollingFilePeriod));
            if(!timeBasePolicyValue.isEmpty()){
                System.out.println("custom");
                logFileAppender=new LogbackCustomRollingFileAppender(timeBasePolicyUnit,timeBasePolicyValue,logFilePolicy,limitRollingFileNumber,path,fileName);
                logFileAppender.rollover();
            }
            logFilePolicy.setParent(logFileAppender);
//            logFilePolicy.start();
            logFileAppender.setRollingPolicy(logFilePolicy);

        }
        else{ // size based rolling file appender
            SizeAndTimeBasedRollingPolicy logFilePolicy=new SizeAndTimeBasedRollingPolicy<>();
            SizeBasedTriggeringPolicy triggeringPolicy=new SizeBasedTriggeringPolicy();
            triggeringPolicy.setContext(context);
            triggeringPolicy.start();
            triggeringPolicy.setMaxFileSize(FileSize.valueOf(sizeBasePolicyValue));

            logFilePolicy.setContext(context);
            logFilePolicy.setMaxFileSize(FileSize.valueOf(sizeBasePolicyValue));
            logFilePolicy.setParent(logFileAppender);
            logFilePolicy.setFileNamePattern(rotatedFileName);
            logFilePolicy.setMaxHistory(MyLogger.getLogbackMaxHistory(deleteRollingFilePeriod));
            logFilePolicy.start();

            logFileAppender.setRollingPolicy(logFilePolicy);
            logFileAppender.setTriggeringPolicy(triggeringPolicy);
//            logFileAppender.start();
        }

        logFileAppender.setContext(context);
        logFileAppender.setName(appenderName);
        logFileAppender.setEncoder(logEncoder);
        logFileAppender.setAppend(true);
        logFileAppender.setFile(path+fileName+".log");
        logFileAppender.start();

//        TimeBasedRollingPolicy rollingFilePolicy=null;
//        SizeAndTimeBasedRollingPolicy sizeAndTimeBasedRollingPolicy=null;
//        SizeBasedTriggeringPolicy sizeBasedTriggeringPolicy=null;
//        RollingFileAppender logFileAppender=null;
//
//        if(rollingPolicy.equals("time")){
//            rollingFilePolicy=new TimeBasedRollingPolicy();
//            rollingFilePolicy.setContext(context);
//            rollingFilePolicy.setFileNamePattern(rotatedFileName);
//            rollingFilePolicy.setMaxHistory(MyLogger.getLogbackMaxHistory(deleteRollingFilePeriod));
//
//            logFileAppender=new LogbackCustomRollingFileAppender.Builder(timeBasePolicyUnit,timeBasePolicyValue)
//                    .limitRollingFileNumber(limitRollingFileNumber)
//                    .rollingPolicy(rollingFilePolicy)
//                    .path(path)
//                    .fileName(fileName)
//                    .build();
//            logFileAppender.rollover();
//        }
//        else if(rollingPolicy.equals("size")){
//            System.out.println("size");
//            sizeAndTimeBasedRollingPolicy=new SizeAndTimeBasedRollingPolicy();
//            sizeAndTimeBasedRollingPolicy.setMaxFileSize(FileSize.valueOf(sizeBasePolicyValue));
//            sizeAndTimeBasedRollingPolicy.setContext(context);
//            sizeAndTimeBasedRollingPolicy.setFileNamePattern(rotatedFileName);
//            sizeAndTimeBasedRollingPolicy.setMaxHistory(MyLogger.getLogbackMaxHistory(deleteRollingFilePeriod));
//
//            sizeBasedTriggeringPolicy=new SizeBasedTriggeringPolicy();
//            sizeBasedTriggeringPolicy.setContext(context);
//            sizeBasedTriggeringPolicy.start();
//            sizeBasedTriggeringPolicy.setMaxFileSize(FileSize.valueOf(sizeBasePolicyValue));
//
//            logFileAppender=new LogbackCustomSizeRollingFileAppender.Builder()
//                    .limitRollingFileNumber(limitRollingFileNumber)
//                    .rollingPolicy(sizeAndTimeBasedRollingPolicy)
//                    .triggeringPolicy(sizeBasedTriggeringPolicy)
//                    .sizeAndTimeBasedPolicy(sizeAndTimeBasedRollingPolicy)
//                    .path(path)
//                    .fileName(fileName)
//                    .build();
//
////            logFileAppender.rollover();
//        }
//
////        LogbackCustomRollingFileAppender logFileAppender=new LogbackCustomRollingFileAppender.Builder(timeBasePolicyUnit,timeBasePolicyValue)
////                .limitRollingFileNumber(limitRollingFileNumber)
////                .rollingPolicy(rollingFilePolicy)
////                .sizeBasedTriggeringPolicy(sizeBasedTriggeringPolicy)
////                .sizeAndTimeBasedPolicy(sizeAndTimeBasedRollingPolicy)
////                .path(path)
////                .fileName(fileName)
////                .build();
//
//        if(rollingFilePolicy!=null){
//            rollingFilePolicy.setParent(logFileAppender);
//            rollingFilePolicy.start();
//            logFileAppender.setRollingPolicy(rollingFilePolicy);
//        }
//        else{
//            sizeAndTimeBasedRollingPolicy.setParent(logFileAppender);
//            sizeAndTimeBasedRollingPolicy.start();
//            sizeBasedTriggeringPolicy.start();
//            logFileAppender.setTriggeringPolicy(sizeBasedTriggeringPolicy);
//            logFileAppender.setRollingPolicy(sizeAndTimeBasedRollingPolicy);
//
//            logFileAppender.setContext(context);
//            logFileAppender.setName(appenderName);
//            logFileAppender.setEncoder(logEncoder);
//            logFileAppender.setAppend(true);
//            logFileAppender.setFile(path+fileName+".log");
//            logFileAppender.start();
//        }

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
    public void timeBaseAppender(){

    }
    public void sizeBaseAppender(){

    }
    public void customTimeBaseAppender(){

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
