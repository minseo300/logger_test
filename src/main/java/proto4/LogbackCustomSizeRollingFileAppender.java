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

public class LogbackCustomSizeRollingFileAppender<E> extends RollingFileAppender<E> {
    private int limitRollingFileNumber;
    private String path;
    private String fileName;
    RollingPolicy rollingPolicy;
    TriggeringPolicy<E> triggeringPolicy=null;

    @Override
    public void rollover()
    {
        System.out.println("rollover");
        try {
            remove();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.rollover();

    }
    public LogbackCustomSizeRollingFileAppender(Builder builder) {
        this.triggeringPolicy = builder.triggeringPolicy;
        this.limitRollingFileNumber = builder.limitRollingFileNumber;
        this.path = builder.path;
        this.fileName = builder.fileName;
        this.rollingPolicy=builder.rollingPolicy;
    }


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
    public static class Builder{
        RollingPolicy rollingPolicy;
        TriggeringPolicy triggeringPolicy;
        private SizeAndTimeBasedRollingPolicy sizeAndTimeBasedRollingPolicy=null;
        private int limitRollingFileNumber;
        private String path;
        private String fileName;

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
        public LogbackCustomSizeRollingFileAppender build(){
            return new LogbackCustomSizeRollingFileAppender(this);
        }
    }
}

