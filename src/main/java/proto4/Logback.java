package proto4;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

// org.slf4j.Logger
public class Logback implements Mlf4j {
    private Logger logger;
    public Logback(String loggerName){
        configure(loggerName);
    }
    public void configure(String loggerName){
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
        logEncoder.setContext(context);
        logEncoder.setPattern("%-12date{YYYY-MM-dd HH:mm:ss.SSS} %-5level – %msg%n");
        logEncoder.start();
        RollingFileAppender logFileAppender = new RollingFileAppender();
        logFileAppender.setContext(context);
        logFileAppender.setName("logFile");
        logFileAppender.setEncoder(logEncoder);
        logFileAppender.setAppend(true);
        logFileAppender.setFile("/Users/iminseo/Desktop/logback/mylogback.log");

        TimeBasedRollingPolicy logFilePolicy = new TimeBasedRollingPolicy();
        logFilePolicy.setContext(context);
        logFilePolicy.setParent(logFileAppender);
        logFilePolicy.setFileNamePattern("/Users/iminseo/Desktop/logback/time-based-logs/logfile-%d{yyyy-MM-dd_HH-mm}.log");
        logFilePolicy.setMaxHistory(7);
        logFilePolicy.start();

        logFileAppender.setRollingPolicy(logFilePolicy);
        logFileAppender.start();

        logger=(Logger) LoggerFactory.getLogger(loggerName);
        logger.setAdditive(false);
        logger.setLevel(Level.INFO);
        logger.addAppender(logFileAppender);
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
