package proto4;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.spi.NoAutoStart;
import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.status.Status;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

@NoAutoStart
public class LogbackCustomTriggeringPolicy<E> implements TimeBasedFileNamingAndTriggeringPolicy<E> {

    @Override
    public void setTimeBasedRollingPolicy(TimeBasedRollingPolicy<E> tbrp) {

    }

    @Override
    public String getElapsedPeriodsFileName() {
        return null;
    }

    @Override
    public String getCurrentPeriodsFileNameWithoutCompressionSuffix() {
        return null;
    }

    @Override
    public ArchiveRemover getArchiveRemover() {
        return null;
    }

    @Override
    public long getCurrentTime() {
        return 0;
    }

    @Override
    public void setCurrentTime(long now) {

    }

    @Override
    public boolean isTriggeringEvent(File activeFile, E event) {
        return false;
    }

    @Override
    public void setContext(Context context) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void addStatus(Status status) {

    }

    @Override
    public void addInfo(String msg) {

    }

    @Override
    public void addInfo(String msg, Throwable ex) {

    }

    @Override
    public void addWarn(String msg) {

    }

    @Override
    public void addWarn(String msg, Throwable ex) {

    }

    @Override
    public void addError(String msg) {

    }

    @Override
    public void addError(String msg, Throwable ex) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return false;
    }
}

