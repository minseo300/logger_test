package proto4;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;

public class LogbackAsyncAppender extends AsyncAppender {
    boolean includeCallerData = false;
    private String FQCN;
    private Logger logger;
    private Level level;


    /**
     * Events of level TRACE, DEBUG and INFO are deemed to be discardable.
     *
     * @param event
     * @return true if the event is of level TRACE, DEBUG or INFO false otherwise.
     */
    protected boolean isDiscardable(ILoggingEvent event) {
        Level level = event.getLevel();
        return level.toInt() <= Level.INFO_INT;
    }

    protected void preprocess(ILoggingEvent eventObject) {
        String msg= eventObject.getFormattedMessage();
        msg="[GUID-123325234532412493] " + msg + "(proto4.Main.main:45)";
        LoggingEvent le=new LoggingEvent(FQCN,logger,level,msg,null,null);
        le.prepareForDeferredProcessing();
        if (includeCallerData)
            le.getCallerData();
    }
    public LogbackAsyncAppender(String fqcn,Logger logger, Level level) {
        this.FQCN=fqcn;
        this.logger=logger;
        this.level=level;
    }

    public boolean isIncludeCallerData() {
        return includeCallerData;
    }

    public void setIncludeCallerData(boolean includeCallerData) {
        this.includeCallerData = includeCallerData;
    }

}
