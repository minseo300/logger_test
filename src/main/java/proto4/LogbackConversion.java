package proto4;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogbackConversion extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        long guid = 123234243503989L;
        return "guid: "+ guid;
    }

}
