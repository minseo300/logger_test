package proto4;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AsyncAppenderBase;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LogbackAsyncAppender2<E> extends LogbackAsyncBase<E> {

    boolean includeCallerData = false;
    public static final int DEFAULT_QUEUE_SIZE = 256;
    int queueSize = DEFAULT_QUEUE_SIZE;
    private String FQCN;
    private Logger logger;
    private Level level;

    BlockingQueue<E> blockingQueue=new ArrayBlockingQueue<>(queueSize);

    int appenderCount = 0;

    static final int UNDEFINED = -1;
    int discardingThreshold = UNDEFINED;
    boolean neverBlock = false;



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

    public LogbackAsyncAppender2(){

    }
    public LogbackAsyncAppender2(String fqcn, Logger logger, Level level) {
        this.FQCN=fqcn;
        this.logger=logger;
        this.level=level;
    }

    private void preprocess2(ILoggingEvent eventObject) {
        eventObject.prepareForDeferredProcessing();
        LoggingEvent le = (LoggingEvent) eventObject;
        String msg=le.getFormattedMessage();
//        msg="[GUID-123325234532412493] " + msg + " (proto4.Main.main:15)";

        Logger logger = (Logger) LoggerFactory.getLogger(le.getLoggerName());
        String FQCN = ch.qos.logback.classic.Logger.class.getName();

        LogbackLoggingEvent newLe = new LogbackLoggingEvent(FQCN,logger,le.getLevel(),msg,null,le.getArgumentArray());
        if (includeCallerData)
            eventObject.getCallerData();
    }

    public void append(E eventObject) {
        if (isQueueBelowDiscardingThreshold() && isDiscardable(eventObject)) {
            return;
        }

        preprocess(eventObject);
        put(eventObject);
    }


    private boolean isQueueBelowDiscardingThreshold() {
        return (blockingQueue.remainingCapacity() < discardingThreshold);
    }

    private void put(E eventObject) {
        if (neverBlock) {
            blockingQueue.offer(eventObject);
        } else {
            putUninterruptibly(eventObject);
        }
    }

    private void putUninterruptibly(E eventObject) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    blockingQueue.put(eventObject);
                    break;
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    public boolean isIncludeCallerData() {
        return includeCallerData;
    }

    public void setIncludeCallerData(boolean includeCallerData) {
        this.includeCallerData = includeCallerData;
    }


}
