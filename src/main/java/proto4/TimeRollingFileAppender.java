package proto4;

import ch.qos.logback.core.rolling.RollingFileAppender;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TimeRollingFileAppender<E> extends RollingFileAppender<E> {
    private static long start = System.currentTimeMillis();
    private int rollOverTimeInterval;
    private int limitRollingFileNumber=0;
    private String path;
    private String fileName;

    public TimeRollingFileAppender(String timeBasePolicyValue,String timeBasePolicyUnit,String path, String fileName){
        // for deleteRollingFilePeriod
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
        this.path=path;
        this.fileName=fileName;
    }
    public TimeRollingFileAppender(String timeBasePolicyValue,String timeBasePolicyUnit,String limitRollingFileNumber,String path,String fileName){
        // for limitRollingFileNumber
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
        this.limitRollingFileNumber= Integer.parseInt(limitRollingFileNumber);
        this.path=path;
        this.fileName=fileName;
    }
    @Override
    public void rollover()
    {
        long currentTime = System.currentTimeMillis();
        int maxIntervalSinceLastLoggingInMillis = rollOverTimeInterval * 1000;
//        rollingPolicy.setParent(this);
//        rollingPolicy.start();
//        System.out.println(this.triggeringPolicy.getClass());
//        System.out.println("rrr "+this.rollOverTimeInterval);
        System.out.println(limitRollingFileNumber);
        if(limitRollingFileNumber!=0) {
            try {
                remove();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if ((currentTime - start) >= maxIntervalSinceLastLoggingInMillis)
        {
            super.rollover();
            start = System.currentTimeMillis();
        }
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
}
