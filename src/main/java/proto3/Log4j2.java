//package proto3;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.core.Logger;
//import org.apache.logging.log4j.core.LoggerContext;
//import org.apache.logging.log4j.core.appender.RollingFileAppender;
//import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
//import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
//import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
//import org.apache.logging.log4j.core.appender.rolling.action.*;
//import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
//import org.apache.logging.log4j.core.config.AppenderRef;
//import org.apache.logging.log4j.core.config.Configuration;
//import org.apache.logging.log4j.core.config.LoggerConfig;
//import org.slf4j.Marker;
//
//import java.util.zip.Deflater;
//
//
////org.slf4j.Logger
//public class Log4j2 implements Mlf4j {
//
//    private Logger logger;
//
//    public Log4j2(String loggerName,String level, String configureMode){
//        if(configureMode.equals("default")) configureDefault(loggerName,level);
//        else configure(loggerName,level, appenderRef);
//    }
//
//    public void configureDefault(String loggerName, String level){
//        String path = "/Users/iminseo/Desktop";
//        String rotatedFileName = path + fileName + MyLogger.getRotateFileNamePattern(timeBasePolicyUnit, rollingPolicy)
//                + ".log";
//        String layoutPattern = "%style{%d{yyyy/MM/dd HH:mm:ss,SSS}}{cyan} %highlight{[%-5p]}{FATAL=bg_red, ERROR=red,\n" +
//                "            INFO=green, DEBUG=blue}  [%class] %style{[%t]}{yellow}- %m%n"; // your pattern here
//
//
//        LoggerContext context=LoggerContext.getContext(false);
//        Configuration configuration = context.getConfiguration();
//        RollingFileAppender.Builder builder = RollingFileAppender.newBuilder();
//
//        builder.withFilePattern(rotatedFileName);
//        builder.withFileName(path + fileName + ".log");
//        builder.withAppend(true);
//        builder.setName(appenderName);
//        builder.setLayout(layout);
//        if (rollingFilePolicy == LogFileRollingPolicy.TIME) {
//            builder.withPolicy(TimeBasedTriggeringPolicy.createPolicy(timeBasePolicyValue, "false"));
//        } else {
//            builder.withPolicy(SizeBasedTriggeringPolicy.createPolicy(sizeBasePolicyValue));
//        }
//        if (!StringUtil.isEmpty(deleteRollingFilePeriod)) {
//            PathCondition[] pathConditions = new PathCondition[2];
//            pathConditions[0] = IfLastModified.createAgeCondition(Duration.parse(deleteRollingFilePeriod));
//            pathConditions[1] = IfFileName.createNameCondition(fileName + "?*log", null);
//            DeleteAction deleteAction = DeleteAction.createDeleteAction(path, false, 1, false, null, pathConditions,
//                    null, configuration);
//            Action[] actions = new Action[1];
//            actions[0] = deleteAction;
//            builder.withStrategy(DefaultRolloverStrategy.newBuilder()
//                    .withCompressionLevelStr(String.valueOf(Deflater.DEFAULT_COMPRESSION)).withCustomActions(actions)
//                    .withConfig(configuration).withMax(limitRollingFileNumber).build());
//        } else {
//            Action[] actions = new Action[1];
//            if (limitRollingFileNumber != null) {
//                PathCondition[] pathConditions = new PathCondition[2];
//                pathConditions[0] = IfFileName.createNameCondition(fileName + "?*log", null);
//                pathConditions[1] = IfAccumulatedFileCount
//                        .createFileCountCondition(Integer.parseInt(limitRollingFileNumber));
//                DeleteAction deleteAction = DeleteAction.createDeleteAction(path, false, 1, false, null, pathConditions,
//                        null, configuration);
//                actions[0] = deleteAction;
//            }
//            builder.withStrategy(DefaultRolloverStrategy.newBuilder()
//                    .withCompressionLevelStr(String.valueOf(Deflater.DEFAULT_COMPRESSION)).withCustomActions(actions)
//                    .withConfig(configuration).withMax(String.valueOf(Integer.MAX_VALUE)).build());
//        }
//        RollingFileAppender rotateFile = builder.build();
//        rotateFile.start();
//        configuration.addAppender(rotateFile);
//
//        AppenderRef ref = AppenderRef.createAppenderRef(appenderName, null, null);
//        AppenderRef[] refs = new AppenderRef[] { ref };
//        LoggerConfig loggerConfig = null;
//        if (async) {
//            loggerConfig = AsyncLoggerConfig.createLogger(additivity, level, loggerName, "true", refs, null,
//                    configuration, null);
//        } else {
//            loggerConfig = LoggerConfig.createLogger(additivity, level, loggerName, "true", refs, null, configuration,
//                    null);
//        }
//        loggerConfig.addAppender(rotateFile, null, null);
//        configuration.addLogger(loggerName, loggerConfig);
//        context.updateLoggers();
//        configuration.start();
//
//        logger= (Logger) LogManager.getLogger(loggerName);
//
//    }
//    public void configure(String loggerName,String level, String appenderRef){
//
//
//    }
//
////    public Logger getLogger(String loggerName){
////        return (Logger) LogManager.getLogger(loggerName);
////    }
//    public void info(String msg){
//        logger.info(String.valueOf(logger.getClass()));
//        logger.info(msg);
//    }
//
//    @Override
//    public void info(String format, Object arg) {
//
//    }
//
//    @Override
//    public void info(String format, Object arg1, Object arg2) {
//
//    }
//
//    @Override
//    public void info(String format, Object... arguments) {
//
//    }
//
//    @Override
//    public void info(String msg, Throwable t) {
//
//    }
//
//    @Override
//    public boolean isInfoEnabled(Marker marker) {
//        return false;
//    }
//
//    @Override
//    public void info(Marker marker, String msg) {
//
//    }
//
//    @Override
//    public void info(Marker marker, String format, Object arg) {
//
//    }
//
//    @Override
//    public void info(Marker marker, String format, Object arg1, Object arg2) {
//
//    }
//
//    @Override
//    public void info(Marker marker, String format, Object... arguments) {
//
//    }
//
//    @Override
//    public void info(Marker marker, String msg, Throwable t) {
//
//    }
//
//    @Override
//    public void finest(String var1) {
//
//    }
//
//    @Override
//    public void serve(String var1) {
//
//    }
//
//    @Override
//    public void config(String var1) {
//
//    }
//
//    @Override
//    public boolean isWarnEnabled() {
//        return false;
//    }
//
//    @Override
//    public void warn(String msg) {
//
//    }
//
//    @Override
//    public void warn(String format, Object arg) {
//
//    }
//
//    @Override
//    public void warn(String format, Object... arguments) {
//
//    }
//
//    @Override
//    public void warn(String format, Object arg1, Object arg2) {
//
//    }
//
//    @Override
//    public void warn(String msg, Throwable t) {
//
//    }
//
//    @Override
//    public boolean isWarnEnabled(Marker marker) {
//        return false;
//    }
//
//    @Override
//    public void warn(Marker marker, String msg) {
//
//    }
//
//    @Override
//    public void warn(Marker marker, String format, Object arg) {
//
//    }
//
//    @Override
//    public void warn(Marker marker, String format, Object arg1, Object arg2) {
//
//    }
//
//    @Override
//    public void warn(Marker marker, String format, Object... arguments) {
//
//    }
//
//    @Override
//    public void warn(Marker marker, String msg, Throwable t) {
//
//    }
//
//    @Override
//    public boolean isErrorEnabled() {
//        return false;
//    }
//
//    @Override
//    public void error(String msg) {
//
//    }
//
//    @Override
//    public void error(String format, Object arg) {
//
//    }
//
//    @Override
//    public void error(String format, Object arg1, Object arg2) {
//
//    }
//
//    @Override
//    public void error(String format, Object... arguments) {
//
//    }
//
//    @Override
//    public void error(String msg, Throwable t) {
//
//    }
//
//    @Override
//    public boolean isErrorEnabled(Marker marker) {
//        return false;
//    }
//
//    @Override
//    public void error(Marker marker, String msg) {
//
//    }
//
//    @Override
//    public void error(Marker marker, String format, Object arg) {
//
//    }
//
//    @Override
//    public void error(Marker marker, String format, Object arg1, Object arg2) {
//
//    }
//
//    @Override
//    public void error(Marker marker, String format, Object... arguments) {
//
//    }
//
//    @Override
//    public void error(Marker marker, String msg, Throwable t) {
//
//    }
//
//    @Override
//    public void fatal(String msg) {
//        logger.fatal(String.valueOf(logger.getClass()));
//        logger.fatal(msg);
//    }
//
//    public String getName() {
//        return null;
//    }
//
//
//
//    @Override
//    public boolean isTraceEnabled() {
//        return false;
//    }
//
//    public void trace(String msg){ logger.trace(msg);}
//
//    @Override
//    public void trace(String format, Object arg) {
//
//    }
//
//    @Override
//    public void trace(String format, Object arg1, Object arg2) {
//
//    }
//
//    @Override
//    public void trace(String format, Object... arguments) {
//
//    }
//
//    @Override
//    public void trace(String msg, Throwable t) {
//
//    }
//
//    @Override
//    public boolean isTraceEnabled(Marker marker) {
//        return false;
//    }
//
//    @Override
//    public void trace(Marker marker, String msg) {
//
//    }
//
//    @Override
//    public void trace(Marker marker, String format, Object arg) {
//
//    }
//
//    @Override
//    public void trace(Marker marker, String format, Object arg1, Object arg2) {
//
//    }
//
//    @Override
//    public void trace(Marker marker, String format, Object... argArray) {
//
//    }
//
//    @Override
//    public void trace(Marker marker, String msg, Throwable t) {
//
//    }
//
//    @Override
//    public boolean isDebugEnabled() {
//        return false;
//    }
//
//    public void debug(String msg){logger.debug(msg);}
//
//    @Override
//    public void debug(String format, Object arg) {
//
//    }
//
//    @Override
//    public void debug(String format, Object arg1, Object arg2) {
//
//    }
//
//    @Override
//    public void debug(String format, Object... arguments) {
//
//    }
//
//    @Override
//    public void debug(String msg, Throwable t) {
//
//    }
//
//    @Override
//    public boolean isDebugEnabled(Marker marker) {
//        return false;
//    }
//
//    @Override
//    public void debug(Marker marker, String msg) {
//
//    }
//
//    @Override
//    public void debug(Marker marker, String format, Object arg) {
//
//    }
//
//    @Override
//    public void debug(Marker marker, String format, Object arg1, Object arg2) {
//
//    }
//
//    @Override
//    public void debug(Marker marker, String format, Object... arguments) {
//
//    }
//
//    @Override
//    public void debug(Marker marker, String msg, Throwable t) {
//
//    }
//
//    @Override
//    public boolean isInfoEnabled() {
//        return false;
//    }
//
//}
