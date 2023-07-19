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
import java.util.Date;

public class LogbackTimeBasedTriggeringPolicy extends TriggeringPolicyBase {
    private int interval;
    protected String elapsedPeriodsFileName;
    protected Date dateInCurrentPeriod = null;
    protected ArchiveRemover archiveRemover = null;
    protected RollingCalendar rc;
    private long currentTime;
//    public LogbackTimeBasedTriggeringPolicy(){
//        archiveRemover=new TimeBasedArchiveRemover()
//    }
    public void setInterval(String timeBasePolicyValue, String timeBasePolicyUnit){
//        if(timeBasePolicyUnit.equals("hour")){
//            this.interval=Integer.parseInt(timeBasePolicyValue)*60*60*1000;
//        }
//        else if(timeBasePolicyUnit.equals("minute")){
//            this.interval=Integer.parseInt(timeBasePolicyValue)*60*1000;
//
//        }
//        else if(timeBasePolicyUnit.equals("second")){
//            this.interval=Integer.parseInt(timeBasePolicyValue)*1000;
//        }
//        else if(timeBasePolicyUnit.equals("day")){
//            this.interval=Integer.parseInt(timeBasePolicyValue)*60*60*24*1000;
//        }
        this.interval=MyLogger.getMillisByTimeValueAndUnit(timeBasePolicyUnit,timeBasePolicyValue);
    }
    public String getElapsedPeriodsFileName() {
        return elapsedPeriodsFileName;
    }
    @Override
    public boolean isTriggeringEvent(File activeFile, Object event) {
        currentTime = System.currentTimeMillis();
        try {
            FileTime creationTime=(FileTime) Files.getAttribute(activeFile.toPath(),"creationTime");
            if(currentTime-creationTime.toMillis()>interval){
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }





}
