package proto4;

import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicyBase;
import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.rolling.helper.RollingCalendar;
import ch.qos.logback.core.rolling.helper.TimeBasedArchiveRemover;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogbackTimeBasedTriggeringPolicy extends TriggeringPolicyBase {
    private int interval;
    protected String elapsedPeriodsFileName;

    private long currentTime;
    private long nextRollOverTime;
    private String timeUnit;
    private String timeValue;
    private SimpleDateFormat dateFormat;
    private boolean isStarted=false;
    public void setInterval(String timeBasePolicyValue, String timeBasePolicyUnit){
        this.timeUnit=timeBasePolicyUnit;
        this.timeValue=timeBasePolicyValue;
        this.interval=MyLogger.getMillisByTimeValueAndUnit(timeBasePolicyUnit,timeBasePolicyValue);
    }
    public String getElapsedPeriodsFileName() {
        return elapsedPeriodsFileName;
    }

    private void setCurrentTime(long now){
        Date dateFromCurrentTime=new Date(now);
        String ret;
        try {
            switch(timeUnit.toUpperCase()) {
                case "D":
                case "DAY":
                    dateFormat=new SimpleDateFormat("yyyyMMdd000000");
                    ret=dateFormat.format(dateFromCurrentTime);
                    dateFromCurrentTime=dateFormat.parse(ret);
                    break;
                case "H":
                case "HOUR":
                    dateFormat=new SimpleDateFormat("yyyyMMddHH0000");
                    ret=dateFormat.format(dateFromCurrentTime);
                    dateFromCurrentTime=dateFormat.parse(ret);
                    break;
                case "M":
                case "MINUTE":
                    dateFormat=new SimpleDateFormat("yyyyMMddHHmm00");
                    ret=dateFormat.format(dateFromCurrentTime);
                    dateFromCurrentTime=dateFormat.parse(ret);
                    break;
                case "S":
                case "SECOND":
                    dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
                    ret=dateFormat.format(dateFromCurrentTime);
                    dateFromCurrentTime=dateFormat.parse(ret);
                    break;
            }
        } catch (ParseException e){
            System.out.println(e);
        }
        this.currentTime=dateFromCurrentTime.getTime();
    }
    @Override
    public boolean isTriggeringEvent(File activeFile, Object event) {
        if(!isStarted) {
            setCurrentTime(System.currentTimeMillis());
            nextRollOverTime=currentTime+interval;
            isStarted=true;
        }
        else {
            currentTime=System.currentTimeMillis();
        }

        if(currentTime>=nextRollOverTime) {
            nextRollOverTime=currentTime+interval;
            return true;
        }
        return false;
    }





}
