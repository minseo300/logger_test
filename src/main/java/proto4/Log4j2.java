package proto4;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.action.*;
import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Marker;

import java.util.Map;
import java.util.zip.Deflater;

//org.slf4j.Logger
public class Log4j2 implements Mlf4j {

    private Logger logger;

    public Log4j2(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params){
        System.out.println(params.getAppender().isEmpty());
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
        String layoutPattern = "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"; // your pattern here


        LoggerContext context=LoggerContext.getContext(false);
        context.reconfigure();
        Configuration configuration = context.getConfiguration();
        RollingFileAppender.Builder builder = RollingFileAppender.newBuilder();
        PatternLayout layout = PatternLayout.newBuilder().withPattern(layoutPattern).build();

        builder.withFilePattern(rotatedFileName);
        builder.withFileName(path + fileName + ".log");
        builder.withAppend(true);
        builder.setName(appenderName);
        builder.setLayout(layout);
        if (rollingPolicy.equals("time")) {
            builder.withPolicy(TimeBasedTriggeringPolicy.createPolicy(timeBasePolicyValue, "false"));
        } else{
            builder.withPolicy(SizeBasedTriggeringPolicy.createPolicy(sizeBasePolicyValue));
        }
        if (!deleteRollingFilePeriod.isEmpty()) {
            PathCondition[] pathConditions = new PathCondition[2];
            pathConditions[0] = IfLastModified.createAgeCondition(Duration.parse(deleteRollingFilePeriod));
            pathConditions[1] = IfFileName.createNameCondition(fileName + "?*log", null);
            DeleteAction deleteAction = DeleteAction.createDeleteAction(path, false, 1, false, null, pathConditions,
                    null, configuration);
            Action[] actions = new Action[1];
            actions[0] = deleteAction;
            builder.withStrategy(DefaultRolloverStrategy.newBuilder()
                    .withCompressionLevelStr(String.valueOf(Deflater.DEFAULT_COMPRESSION)).withCustomActions(actions)
                    .withConfig(configuration).withMax(limitRollingFileNumber).build());
        } else {
            Action[] actions = new Action[1];
            if (limitRollingFileNumber != null) {
                PathCondition[] pathConditions = new PathCondition[2];
                pathConditions[0] = IfFileName.createNameCondition(fileName + "?*log", null);
                pathConditions[1] = IfAccumulatedFileCount
                        .createFileCountCondition(Integer.parseInt(limitRollingFileNumber));
                DeleteAction deleteAction = DeleteAction.createDeleteAction(path, false, 1, false, null, pathConditions,
                        null, configuration);
                actions[0] = deleteAction;
            }
            builder.withStrategy(DefaultRolloverStrategy.newBuilder()
                    .withCompressionLevelStr(String.valueOf(Deflater.DEFAULT_COMPRESSION)).withCustomActions(actions)
                    .withConfig(configuration).withMax(String.valueOf(Integer.MAX_VALUE)).build());
        }
        RollingFileAppender rotateFile = builder.build();
        rotateFile.start();
        configuration.addAppender(rotateFile);

        AppenderRef ref = AppenderRef.createAppenderRef(appenderName, null, null);
        AppenderRef[] refs = new AppenderRef[] { ref };
        LoggerConfig loggerConfig = null;
        if (async) {
            loggerConfig = AsyncLoggerConfig.createLogger(additivity, level, loggerName, "true", refs, null,
                    configuration, null);
        } else {
            loggerConfig = LoggerConfig.createLogger(additivity, level, loggerName, "true", refs, null, configuration,
                    null);
        }
        loggerConfig.addAppender(rotateFile, null, null);
        configuration.addLogger(loggerName, loggerConfig);
        context.updateLoggers();
        configuration.start();

        this.logger= (Logger) LogManager.getLogger(loggerName);
    }
    public void configureConsole(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params){

        LoggerContext context=(LoggerContext) LogManager.getContext(false);
        Configuration configuration= context.getConfiguration();
        Boolean async=params.getAsync();
        Level level= Level.valueOf(params.getLevel());

        AppenderRef ref=AppenderRef.createAppenderRef(appenderName, level,null); // appenderName, level, filter
//        System.out.println("[initial.Log4j2] - (configure) - ref level: "+ref.getLevel());
        AppenderRef[] refs=new AppenderRef[]{ref};
        ConsoleAppender.Builder builder=ConsoleAppender.newBuilder();
        builder.setLayout(null);
        builder.setName(appenderName);
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
        LoggerConfig loggerConfig=LoggerConfig.createLogger(false, level,loggerName,"true",refs,null,configuration,null);

        loggerConfig.addAppender(appender,level,null);
        configuration.addLogger(loggerName,loggerConfig);
        Map<String, Appender> appenderMap=configuration.getAppenders();

        context.updateLoggers();
        configuration.start();
         this.logger= (Logger) LogManager.getLogger(loggerName);
    }

    public Logger getLogger(String loggerName){
        return (Logger) LogManager.getLogger(loggerName);
    }
    public void info(String msg){
        this.logger.info(String.valueOf(logger.getClass()));
        this.logger.info(String.valueOf(logger.getAppenders().get("defaultLogger1Appender").getClass()));
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
