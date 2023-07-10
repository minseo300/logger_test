package proto4;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.logging.log4j.util.StackLocatorUtil;

public class StackTraceConversion extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        StringBuilder logDebugBuilder=new StringBuilder();
        StackTraceElement source= StackLocatorUtil.calcLocation(MyLogger.class.getName());
        if(source==null) return null;

        String declaringClass= source.getClassName();
        int line=source.getLineNumber();
        String method= source.getMethodName();

        logDebugBuilder.append(" (").append(declaringClass).append(":").append(method).append(":").append(line).append(")");
        String logDebug=logDebugBuilder.toString();

        return logDebug;
    }
}
