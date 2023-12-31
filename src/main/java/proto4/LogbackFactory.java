//package proto4;
//
//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
//import ch.qos.logback.core.CoreConstants;
//import ch.qos.logback.core.rolling.RollingFileAppender;
//import ch.qos.logback.core.rolling.RollingPolicyBase;
//import ch.qos.logback.core.rolling.TriggeringPolicyBase;
//import ch.qos.logback.core.util.FileSize;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class LogbackFactory {
//
//    public RollingFileAppender create(String fileName, String loggerName, String appenderName, Boolean additivity, Loggers params,String formatter,LoggerContext context){
//        String path = params.getPath();
//        String timeBasePolicyUnit=params.getTimeBasePolicyUnit();
//        String rollingPolicy=params.getRollingPolicy();
//        String rotatedFileName = path + fileName + MyLogger.getRotateFileNamePattern(timeBasePolicyUnit, rollingPolicy)
//                + ".log";
//        String timeBasePolicyValue=params.getTimeBasePolicyValue();
//        String deleteRollingFilePeriod=params.getDeleteRollingFilePeriod();
//        String sizeBasePolicyValue=params.getSizeBasePolicyValue();
//        String limitRollingFileNumber=params.getLimitRollingFileNumber();
//
//        // create PatternLayoutEncoder
//        LogbackPatternLayoutEncoder layoutEncoder=createLayoutEncoder(context);
//
//        // create RollingFileAppender
//        RollingFileAppender rollingFileAppender=new RollingFileAppender();
//        rollingFileAppender.setContext(context);
//        rollingFileAppender.setFile(path+fileName+".log");
//        rollingFileAppender.setEncoder(layoutEncoder);
//        rollingFileAppender.setAppend(true);
//        rollingFileAppender.setName(appenderName);
//
//        // create RollingPolicy
//        RollingPolicyBase rollingPolicyBase=createRollingPolicy(timeBasePolicyUnit,fileName,rollingPolicy,context,rotatedFileName,deleteRollingFilePeriod,limitRollingFileNumber);
//        rollingPolicyBase.setParent(rollingFileAppender);
//        rollingPolicyBase.setContext(context);
//        rollingPolicyBase.setFileNamePattern(rotatedFileName);
//        rollingPolicyBase.start();
//
//        // create TriggeringPolicy
//        TriggeringPolicyBase triggeringPolicyBase=createTriggeringPolicy(rollingPolicy,context,sizeBasePolicyValue,timeBasePolicyValue,timeBasePolicyUnit);
//        triggeringPolicyBase.setContext(context);
//        triggeringPolicyBase.start();
//
//        // starting rollingFileAppender after setting rollingPolicy and triggeringPolicy to appender
//        rollingFileAppender.setTriggeringPolicy(triggeringPolicyBase);
//        rollingFileAppender.setRollingPolicy(rollingPolicyBase);
//        rollingFileAppender.start();
//
//        return rollingFileAppender;
//    }
//    public TriggeringPolicyBase createTriggeringPolicy(String rollingPolicy,LoggerContext context,String sizeBasePolicyValue,String timeBasePolicyValue,String timeBasePolicyUnit){
//        TriggeringPolicyBase triggeringPolicyBase=null;
//        if(rollingPolicy.equals("time")){
//            triggeringPolicyBase=new LogbackTimeBasedTriggeringPolicy();
//            ((LogbackTimeBasedTriggeringPolicy) triggeringPolicyBase).setInterval(timeBasePolicyValue,timeBasePolicyUnit);
//        }
//        else{
//            triggeringPolicyBase=new LogbackSizeBasedTriggeringPolicy();
//            ((LogbackSizeBasedTriggeringPolicy) triggeringPolicyBase).setMaxFileSize(FileSize.valueOf(sizeBasePolicyValue));
//        }
//
//        return triggeringPolicyBase;
//    }
//    public RollingPolicyBase createRollingPolicy(String intervalUnit,String fileName,String rollingPolicy,LoggerContext context,String fileNamePattern,String deleteByPeriod, String deleteByFileNumber){
//        RollingPolicyBase rollingPolicyBase=null;
//        if(rollingPolicy.equals("time")){
//            rollingPolicyBase=new LogbackTimeBasedRollingPolicy(intervalUnit,deleteByPeriod,deleteByFileNumber);
//        }
//        else{
//            rollingPolicyBase=new LogbackSizeBasedRollingPolicy(deleteByPeriod,deleteByFileNumber);
//        }
//
//
//        return rollingPolicyBase;
//    }
//    public LogbackPatternLayoutEncoder createLayoutEncoder(LoggerContext context){
//        Map<String, String> ruleRegistry = (Map) context.getObject(CoreConstants.PATTERN_RULE_REGISTRY);
//        if (ruleRegistry == null) {
//            ruleRegistry = new HashMap<String, String>();
//        }
//        context.putObject(CoreConstants.PATTERN_RULE_REGISTRY, ruleRegistry);
//        String conversionWord = "guid";
//        String converterClass = "proto4.LogbackConversion";
//        ruleRegistry.put(conversionWord, converterClass);
//
//        String conversionWord2 = "stack";
//        String converterClass2 = "proto4.StackTraceConversion";
//        ruleRegistry.put(conversionWord2, converterClass2);
//
//
//        LogbackPatternLayoutEncoder logEncoder = new LogbackPatternLayoutEncoder();
//        String layoutPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %guid [%t] %c{1} - %msg %-6stack\n"; // your pattern here.
//        String layoutPattern = "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"; // your pattern here
//        logEncoder.setPattern(layoutPattern);
//
//        logEncoder.setContext(context);
//        logEncoder.start();
//
//        return logEncoder;
//    }
//}
