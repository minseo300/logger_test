import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.rolling.*;
import ch.qos.logback.core.util.FileSize;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LogbackTest {

    public static void main(String[] args) {
        LoggerContext logCtx=(LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
        logEncoder.setContext(logCtx);
        logEncoder.setPattern("%-12date{YYYY-MM-dd HH:mm:ss.SSS} %-5level - %msg%n");
        logEncoder.start();

        logEncoder = new PatternLayoutEncoder();
        logEncoder.setContext(logCtx);
        logEncoder.setPattern("%-12date{YYYY-MM-dd HH:mm:ss.SSS} %-5level - %msg%n");
        logEncoder.start();

        RollingFileAppender logFileAppender = new RollingFileAppender();
        logFileAppender.setContext(logCtx);
        logFileAppender.setName("logFile");
        logFileAppender.setEncoder(logEncoder);
        logFileAppender.setAppend(true);
        logFileAppender.setFile("/Users/iminseo/Desktop/logs/myLog.log");

        FixedWindowRollingPolicy logFilePolicy = new FixedWindowRollingPolicy();
//        SizeAndTimeBasedRollingPolicy logFilePolicy=new SizeAndTimeBasedRollingPolicy();
        logFilePolicy.setContext(logCtx);
        logFilePolicy.setParent(logFileAppender);
        logFilePolicy.setFileNamePattern("/Users/iminseo/Desktop/logs/-%i.log");
//        logFilePolicy.setMaxFileSize(FileSize.valueOf("1kb"));
        logFilePolicy.setMaxIndex(100);
        logFilePolicy.setMinIndex(1);
        logFilePolicy.start();

        SizeBasedTriggeringPolicy triggeringPolicy=new SizeBasedTriggeringPolicy();
        triggeringPolicy.setContext(logCtx);
        triggeringPolicy.setMaxFileSize(FileSize.valueOf("1kb"));
        triggeringPolicy.start();

        logFileAppender.setRollingPolicy(logFilePolicy);
        logFileAppender.setTriggeringPolicy(triggeringPolicy);
        logFileAppender.start();

        Logger log = logCtx.getLogger(LogbackTest.class);
        log.setAdditive(false);
        log.setLevel(Level.INFO);
        log.addAppender(logFileAppender);

        for (int i = 0; i < 10000; i++) {
            log.info("Rolling file appender example..."+i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
