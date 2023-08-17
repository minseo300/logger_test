//package proto4;
//
//import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
//import ch.qos.logback.core.rolling.TriggeringPolicyBase;
//import ch.qos.logback.core.rolling.helper.ArchiveRemover;
//import ch.qos.logback.core.rolling.helper.RollingCalendar;
//import ch.qos.logback.core.rolling.helper.TimeBasedArchiveRemover;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.attribute.FileTime;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//public class LogbackTimeBasedTriggeringPolicy extends TriggeringPolicyBase {
//    private int interval;
//    protected String elapsedPeriodsFileName;
//
//    private long currentTime;
//    private long nextRollOverTime;
//    private String timeUnit;
//    private String timeValue;
//    private Calendar cal=Calendar.getInstance();
//    private boolean isStarted=false;
//    public void setInterval(String timeBasePolicyValue, String timeBasePolicyUnit){
//        this.timeUnit=timeBasePolicyUnit;
//        this.timeValue=timeBasePolicyValue;
//        this.interval=MyLogger.getMillisByTimeValueAndUnit(timeBasePolicyUnit,timeBasePolicyValue);
//    }
//    public String getElapsedPeriodsFileName() {
//        return elapsedPeriodsFileName;
//    }
//
//    private void setCurrentTime(long now){
//        cal.setTime(new Date(now));
//        switch(timeUnit.toUpperCase()) {
//            case "D":
//            case "DAY":
//                cal.set(Calendar.HOUR,0);
//                cal.set(Calendar.MINUTE,0);
//                cal.set(Calendar.SECOND,0);
//                cal.set(Calendar.MILLISECOND,0);
//                break;
//            case "H":
//            case "HOUR":
//                cal.set(Calendar.MINUTE,0);
//                cal.set(Calendar.SECOND,0);
//                cal.set(Calendar.MILLISECOND,0);
//                break;
//            case "M":
//            case "MINUTE":
//                cal.set(Calendar.SECOND,0);
//                cal.set(Calendar.MILLISECOND,0);
//                break;
//            case "S":
//            case "SECOND":
//                cal.set(Calendar.MILLISECOND,0);
//                break;
//        }
//        Date date=cal.getTime();
//        this.currentTime=date.getTime();
//    }
//    @Override
//    public boolean isTriggeringEvent(File activeFile, Object event) {
//        setCurrentTime(System.currentTimeMillis());
//        if(!isStarted) {
//            nextRollOverTime=currentTime+interval;
//            isStarted=true;
//        }
//        else {
//            currentTime=System.currentTimeMillis();
//        }
//
//        if(currentTime>=nextRollOverTime) {
//            setCurrentTime(currentTime);
//            nextRollOverTime=currentTime+interval;
//            return true;
//        }
//        return false;
//    }
//
//
//
//
//
//}
