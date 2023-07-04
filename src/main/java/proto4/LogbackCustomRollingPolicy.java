package proto4;


import ch.qos.logback.core.rolling.TimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

class LogbackCustomRollingPolicy<E> extends TimeBasedRollingPolicy<E> {
    private int periodMinutes;

    public int getPeriodMinutes() {
        return this.periodMinutes;
    }

    public void setPeriodMinutes(int minutes) {
        this.periodMinutes = minutes;
    }

    @Override
    public void start() {
        LogbackCustomRollingPolicy<E> triggeringPolicy = new LogbackCustomRollingPolicy<E>();
        triggeringPolicy.setPeriodMinutes(periodMinutes);
        setTimeBasedFileNamingAndTriggeringPolicy((TimeBasedFileNamingAndTriggeringPolicy<E>) triggeringPolicy);

        super.start();
    }

    @Override
    public String toString() {
        return "MyRollingPolicy";
    }
}

