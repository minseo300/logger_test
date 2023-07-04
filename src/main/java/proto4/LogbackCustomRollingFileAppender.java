package proto4;

import ch.qos.logback.core.rolling.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LogbackCustomRollingFileAppender<E> extends RollingFileAppender<E> {
    private static long start = System.currentTimeMillis();
    private int rollOverTimeInterval;
    private int limitRollingFileNumber;
    private String path;
    private String fileName;
    RollingPolicy rollingPolicy;
    TriggeringPolicy<E> triggeringPolicy=null;

    public LogbackCustomRollingFileAppender(Builder builder) {
        this.triggeringPolicy = builder.triggeringPolicy;
        this.limitRollingFileNumber = builder.limitRollingFileNumber;
        this.path = builder.path;
        this.fileName = builder.fileName;
        this.rollingPolicy=builder.rollingPolicy;
        this.rollOverTimeInterval= builder.rollOverTimeInterval;
    }

    @Override
    public void rollover()
    {
        long currentTime = System.currentTimeMillis();
        int maxIntervalSinceLastLoggingInMillis = rollOverTimeInterval * 1000;
//        rollingPolicy.setParent(this);
//        rollingPolicy.start();
//        System.out.println(this.triggeringPolicy.getClass());
        System.out.println("rrr "+this.rollOverTimeInterval);
        try {
            remove();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if ((currentTime - start) >= maxIntervalSinceLastLoggingInMillis)
        {
            super.rollover();
            start = System.currentTimeMillis();
        }
    }
    public static class Builder {
        private int rollOverTimeInterval;
        RollingPolicy rollingPolicy;
        TriggeringPolicy triggeringPolicy;
        private SizeAndTimeBasedRollingPolicy sizeAndTimeBasedRollingPolicy=null;
        private int limitRollingFileNumber;
        private String path;
        private String fileName;

        public Builder(String timeBasePolicyUnit,String timeBasePolicyValue) {
            if(timeBasePolicyUnit.equals("hour")){
                this.rollOverTimeInterval=Integer.parseInt(timeBasePolicyValue)*60*60;
            }
            else if(timeBasePolicyUnit.equals("minute")){
                this.rollOverTimeInterval=Integer.parseInt(timeBasePolicyValue)*60;
            }
            else if(timeBasePolicyUnit.equals("second")){
                this.rollOverTimeInterval=Integer.parseInt(timeBasePolicyValue);
            }
            else if(timeBasePolicyUnit.equals("day")){
                this.rollOverTimeInterval=Integer.parseInt(timeBasePolicyValue)*60*60*24;
            }
            System.out.println(this.rollOverTimeInterval);
        }

        public Builder rollingPolicy(RollingPolicy rollingPolicy) {
            this.rollingPolicy = rollingPolicy;
            return this;
        }

        public Builder limitRollingFileNumber(String limitRollingFileNumber) {
            this.limitRollingFileNumber = Integer.parseInt(limitRollingFileNumber);
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder triggeringPolicy(SizeBasedTriggeringPolicy triggeringPolicy) {
            this.triggeringPolicy = triggeringPolicy;
            return this;
        }
        public Builder sizeAndTimeBasedPolicy(SizeAndTimeBasedRollingPolicy sizeAndTimeBasedRollingPolicy){
            this.rollingPolicy=sizeAndTimeBasedRollingPolicy;
            return this;
        }
        public LogbackCustomRollingFileAppender build() {
            return new LogbackCustomRollingFileAppender(this);
        }
    }


//    public LogbackCustomRollingFileAppender(String timeBasePolicyUnit,String timeBasePolicyValue,TimeBasedRollingPolicy rollingPolicy,String limitRollingFileNumber,String path,String fileName){
//        if(timeBasePolicyUnit.equals("hour")){
//            this.rollOverTimeInterval=Integer.parseInt(timeBasePolicyValue)*60*60;
//        }
//        else if(timeBasePolicyUnit.equals("minute")){
//            this.rollOverTimeInterval=Integer.parseInt(timeBasePolicyValue)*60;
//
//        }
//        else if(timeBasePolicyUnit.equals("second")){
//            this.rollOverTimeInterval=Integer.parseInt(timeBasePolicyValue);
//        }
//        else if(timeBasePolicyUnit.equals("day")){
//            this.rollOverTimeInterval=Integer.parseInt(timeBasePolicyValue)*60*60*24;
//        }
//        this.rollingPolicy=rollingPolicy;
//        this.limitRollingFileNumber= Integer.parseInt(limitRollingFileNumber);
//        this.path=path;
//        this.fileName=fileName;
//    }


    public void remove() throws IOException {
        System.out.println("remove");
//        if(this.triggeringPolicy.isStarted()&&this.triggeringPolicy!=null)System.out.println(this.triggeringPolicy.isStarted());
//        if(this.rollingPolicy.isStarted()) System.out.println(this.rollingPolicy.isStarted());
        File dir = new File(path);
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File f, String name) {
                return name.startsWith(fileName+"_");
            }
        };
        List<File> files= Arrays.asList(dir.listFiles(filter));
        Collections.sort(files);
        if(files.size()>this.limitRollingFileNumber){
            for(int i=0;i<files.size()-this.limitRollingFileNumber;i++)
                Files.deleteIfExists(Paths.get(files.get(i).getPath()));
        }
    }
}

//package proto4;
//
//import ch.qos.logback.core.rolling.RollingFileAppender;
//import ch.qos.logback.core.rolling.RollingPolicy;
//import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
//import ch.qos.logback.core.rolling.TriggeringPolicy;
//
//import java.io.File;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//public class LogbackCustomRollingFileAppender<E> extends RollingFileAppender<E> {
//    private static long start = System.currentTimeMillis();
//    private int rollOverTimeInterval;
//    TimeBasedRollingPolicy rollingPolicy;
//    private int limitRollingFileNumber;
//    private String path;
//    private String fileName;
//
//    public LogbackCustomRollingFileAppender(String timeBasePolicyUnit,String timeBasePolicyValue,TimeBasedRollingPolicy rollingPolicy,String limitRollingFileNumber,String path,String fileName){
//        if(timeBasePolicyUnit.equals("hour")){
//            this.rollOverTimeInterval=Integer.parseInt(timeBasePolicyValue)*60*60;
//        }
//        else if(timeBasePolicyUnit.equals("minute")){
//            this.rollOverTimeInterval=Integer.parseInt(timeBasePolicyValue)*60;
//
//        }
//        else if(timeBasePolicyUnit.equals("second")){
//            this.rollOverTimeInterval=Integer.parseInt(timeBasePolicyValue);
//        }
//        else if(timeBasePolicyUnit.equals("day")){
//            this.rollOverTimeInterval=Integer.parseInt(timeBasePolicyValue)*60*60*24;
//        }
//        this.rollingPolicy=rollingPolicy;
//        this.limitRollingFileNumber= Integer.parseInt(limitRollingFileNumber);
//        this.path=path;
//        this.fileName=fileName;
//    }
//    @Override
//    public void rollover()
//    {
//        long currentTime = System.currentTimeMillis();
//        int maxIntervalSinceLastLoggingInMillis = rollOverTimeInterval * 1000;
////        rollingPolicy.setMaxHistory(5);
//        rollingPolicy.setParent(this);
//        rollingPolicy.start();
//        try {
//            remove();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        if ((currentTime - start) >= maxIntervalSinceLastLoggingInMillis)
//        {
//            super.rollover();
//            start = System.currentTimeMillis();
//        }
//    }
//
//    public void remove() throws IOException {
//        File dir = new File(path);
//        FilenameFilter filter = new FilenameFilter() {
//            public boolean accept(File f, String name) {
//                return name.startsWith(fileName+"_");
//            }
//        };
//        List<File> files= Arrays.asList(dir.listFiles(filter));
//        Collections.sort(files);
//        if(files.size()>this.limitRollingFileNumber){
//            for(int i=0;i<files.size()-this.limitRollingFileNumber;i++)
//                Files.deleteIfExists(Paths.get(files.get(i).getPath()));
//        }
//
//    }
//}

