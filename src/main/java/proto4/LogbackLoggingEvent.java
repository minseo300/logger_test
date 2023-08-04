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
    public String getLogDebug() {
        return this.logDebug;
    }

    private void setPrePostMessage(){
        guid="neeeeeeeew";
        logDebug = " (gooooodo)";
    }
}
