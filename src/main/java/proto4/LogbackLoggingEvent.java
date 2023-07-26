package proto4;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;


public class LogbackLoggingEvent extends LoggingEvent {
    private String guid;
    private String logDebug;

    public String getGuid() {
        return this.guid;
    }

    public LogbackLoggingEvent(String fqcn, Logger logger, Level level, String message, Throwable throwable, Object[] argArray) {
        super(fqcn, logger, level, message, throwable, argArray);
        setPrePostMessage();
    }

    private void setPrePostMessage(){
        guid="123093205932012312";
        logDebug = " (qwerqwerqwerqwer)";
    }
}
