package proto4;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogbackConversion extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String ret="";
        if(event instanceof LogbackLoggingEvent) {
            LogbackLoggingEvent le = (LogbackLoggingEvent) event;
            String guid = le.getGuid();

            if(guid!=null) {
                ret="[GUID-"+guid+"]";
            }
        }
        return ret;
    }

}
