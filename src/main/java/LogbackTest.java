//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.Logger;
//import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
//import ch.qos.logback.classic.joran.JoranConfigurator;
//import ch.qos.logback.classic.spi.ILoggingEvent;
//import ch.qos.logback.classic.util.ContextInitializer;
//import ch.qos.logback.core.Appender;
////import ch.qos.logback.core.joran.GenericXMLConfigurator;
////import ch.qos.logback.core.joran.JoranConstants;
////import ch.qos.logback.core.joran.spi.JoranException;
//import ch.qos.logback.core.CoreConstants;
//import ch.qos.logback.core.joran.spi.InterpretationContext;
//import ch.qos.logback.core.joran.spi.JoranException;
//import ch.qos.logback.core.joran.spi.RuleStore;
////import ch.qos.logback.core.joran.spi.SaxEventInterpreter;
////import ch.qos.logback.core.model.processor.ModelInterpretationContext;
//import ch.qos.logback.core.rolling.*;
//import ch.qos.logback.core.util.FileSize;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.core.config.Configuration;
//import org.apache.logging.log4j.core.config.LoggerConfig;
//import org.slf4j.ILoggerFactory;
//import org.slf4j.LoggerFactory;
//
//import java.net.URL;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//public class LogbackTest {
//    Logger logger;
//    public static void main(String[] args) throws JoranException {
//        Logger logger = (Logger) LoggerFactory.getLogger(LogbackTest.class);
//        logger.info("hi");
//        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//        context.removeObject(CoreConstants.FA_FILENAME_COLLISION_MAP);
//        Map<String, String> map = (Map<String, String>) context.getObject(CoreConstants.FA_FILENAME_COLLISION_MAP);
////        Appender appender = (Appender) context.getObject("Console");
//
//
////        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
////        JoranConfigurator configurator = new JoranConfigurator();
////        configurator.setContext(context);
////        ContextInitializer ci = new ContextInitializer(context);
////        URL url = ci.findURLOfDefaultConfigurationFile(true);
////        configurator.doConfigure(url);
////
////        InterpretationContext ic = configurator.getInterpretationContext();
////        Map<String, Object> objectMap = ic.getObjectMap();
////        Map<String, Appender> appenderMap = (Map<String, Appender>) objectMap.get("APPENDER_BAG");
////        for( String strKey : appenderMap.keySet() ){
////            Appender appender = appenderMap.get(strKey);
////            logger.addAppender(appender);
////        }
////        logger.setAdditive(false);
////        logger.info("HEELLOOO");
//
////        LoggerContext logCtx=(LoggerContext) LoggerFactory.getILoggerFactory();
//
////        PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
////        logEncoder.setContext(logCtx);
////        logEncoder.setPattern("%-12date{YYYY-MM-dd HH:mm:ss.SSS} %-5level - %msg%n");
////        logEncoder.start();
////
////        logEncoder = new PatternLayoutEncoder();
////        logEncoder.setContext(logCtx);
////        logEncoder.setPattern("%-12date{YYYY-MM-dd HH:mm:ss.SSS} %-5level - %msg%n");
////        logEncoder.start();
////
////        RollingFileAppender logFileAppender = new RollingFileAppender();
////        logFileAppender.setContext(logCtx);
////        logFileAppender.setName("logFile");
////        logFileAppender.setEncoder(logEncoder);
////        logFileAppender.setAppend(true);
////        logFileAppender.setFile("/Users/iminseo/Desktop/logs/myLog.log");
////
////        FixedWindowRollingPolicy logFilePolicy = new FixedWindowRollingPolicy();
//////        SizeAndTimeBasedRollingPolicy logFilePolicy=new SizeAndTimeBasedRollingPolicy();
////        logFilePolicy.setContext(logCtx);
////        logFilePolicy.setParent(logFileAppender);
////        logFilePolicy.setFileNamePattern("/Users/iminseo/Desktop/logs/-%i.log");
//////        logFilePolicy.setMaxFileSize(FileSize.valueOf("1kb"));
////        logFilePolicy.setMaxIndex(100);
////        logFilePolicy.setMinIndex(1);
////        logFilePolicy.start();
////
////        SizeBasedTriggeringPolicy triggeringPolicy=new SizeBasedTriggeringPolicy();
////        triggeringPolicy.setContext(logCtx);
////        triggeringPolicy.setMaxFileSize(FileSize.valueOf("1kb"));
////        triggeringPolicy.start();
////
////        logFileAppender.setRollingPolicy(logFilePolicy);
////        logFileAppender.setTriggeringPolicy(triggeringPolicy);
////        logFileAppender.start();
////
////        Logger log = logCtx.getLogger(LogbackTest.class);
////        log.setAdditive(false);
////        log.setLevel(Level.INFO);
////        log.addAppender(logFileAppender);
//
////        for (int i = 0; i < 10000; i++) {
////            log.info("Rolling file appender example..."+i);
////            try {
////                Thread.sleep(500);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////        }
//    }
//}
