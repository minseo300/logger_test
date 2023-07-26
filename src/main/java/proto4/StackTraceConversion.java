package proto4;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.logging.log4j.util.StackLocatorUtil;

public class StackTraceConversion extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        StringBuilder logDebugBuilder=new StringBuilder();
        String ret=null;
        if(event instanceof LogbackLoggingEvent) {
            LogbackLoggingEvent le = (LogbackLoggingEvent) event;

            if(le.getLogDebug()!=null) {
                ret=le.getLogDebug();
            }
        }


        return ret;
    }
}
