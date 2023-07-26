package proto4;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AsyncAppenderBase;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.WarnStatus;
import ch.qos.logback.core.util.COWArrayList;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LogbackAsyncBase<E> extends UnsynchronizedAppenderBase<E> implements AppenderAttachable<E> {
    public static final int DEFAULT_QUEUE_SIZE = 256;
    int queueSize = DEFAULT_QUEUE_SIZE;
    int appenderCount = 0;
    final private COWArrayList<Appender<E>> appenderList = new COWArrayList<Appender<E>>(new Appender[0]);


    BlockingQueue<E> blockingQueue = new ArrayBlockingQueue<E>(queueSize);
    static final int UNDEFINED = -1;
    int discardingThreshold = UNDEFINED;
    Worker worker = new Worker();
    AppenderAttachableImpl<E> aai = new AppenderAttachableImpl<E>();
    boolean neverBlock = false;


    class Worker extends Thread {

        public void run() {
            LogbackAsyncBase<E> parent = LogbackAsyncBase.this;
            AppenderAttachableImpl<E> aai = parent.aai;

            // loop while the parent is started
            while (parent.isStarted()) {
                try {
                    List<E> elements = new ArrayList<E>();
                    E e0 = parent.blockingQueue.take();
                    elements.add(e0);
                    parent.blockingQueue.drainTo(elements);
                    for (E e : elements) {
                        aai.appendLoopOnAppenders(e);
                    }
                } catch (InterruptedException e1) {
                    // exit if interrupted
                    break;
                }
            }

            addInfo("Worker thread will flush remaining events before exiting. ");

            for (E e : parent.blockingQueue) {
                aai.appendLoopOnAppenders(e);
                parent.blockingQueue.remove(e);
            }

            aai.detachAndStopAllAppenders();
        }
    }

    @Override
    public void append(E eventObject) {
        if (isQueueBelowDiscardingThreshold() && isDiscardable(eventObject)) {
            return;
        }


        preprocess(eventObject);
        put(eventObject);
    }
    protected void preprocess(E eventObject) {
    }
    protected boolean isDiscardable(E eventObject) {
        return false;
    }
    private boolean isQueueBelowDiscardingThreshold() {
        return (blockingQueue.remainingCapacity() < discardingThreshold);
    }
    public void doAppend(E eventObject) {
        LoggingEvent le = (LoggingEvent) eventObject;
        String msg=le.getFormattedMessage();
//        msg="[GUID-123325234532412493] " + msg + " (proto4.Main.main:15)";

        Logger logger = (Logger) LoggerFactory.getLogger(le.getLoggerName());
        String FQCN = ch.qos.logback.classic.Logger.class.getName();

        LogbackLoggingEvent newLe = new LogbackLoggingEvent(FQCN,logger,le.getLevel(),msg,null,le.getArgumentArray());

        super.doAppend((E) newLe);
    }

    private void put(E eventObject) {
        if (neverBlock) {
            blockingQueue.offer(eventObject);
        } else {
            putUninterruptibly(eventObject);
        }
    }
    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
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

    @Override
    public void addAppender(Appender<E> newAppender) {
        if (newAppender == null) {
            throw new IllegalArgumentException("Null argument disallowed");
        }
        appenderList.addIfAbsent(newAppender);
    }

    @Override
    public Iterator<Appender<E>> iteratorForAppenders() {
        return null;
    }

    @Override
    public Appender<E> getAppender(String name) {
        return null;
    }

    @Override
    public boolean isAttached(Appender<E> appender) {
        return false;
    }

    @Override
    public void detachAndStopAllAppenders() {

    }

    @Override
    public boolean detachAppender(Appender<E> appender) {
        return false;
    }

    @Override
    public boolean detachAppender(String name) {
        return false;
    }

    @Override
    public void start() {
        if (isStarted())
            return;
        if (appenderCount == 0) {
            addError("No attached appenders found.");
            return;
        }
        if (queueSize < 1) {
            addError("Invalid queue size [" + queueSize + "]");
            return;
        }
        blockingQueue = new ArrayBlockingQueue<E>(queueSize);

        if (discardingThreshold == UNDEFINED)
            discardingThreshold = queueSize / 5;
        addInfo("Setting discardingThreshold to " + discardingThreshold);
        worker.setDaemon(true);
        worker.setName("AsyncAppender-Worker-" + getName());
        // make sure this instance is marked as "started" before staring the worker
        // Thread
        super.start();
        worker.start();
    }
}
