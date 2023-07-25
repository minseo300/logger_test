package proto4;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.rolling.RollingPolicyBase;
import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.rolling.helper.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static ch.qos.logback.core.CoreConstants.CODES_URL;
import static ch.qos.logback.core.CoreConstants.UNBOUND_HISTORY;

public class LogbackTimeBasedRollingPolicy extends RollingPolicyBase {
    private RenameUtil renameUtil = new RenameUtil();
    protected Date dateInCurrentPeriod = null;

    String fileNamePatternStr;
    static final String FNP_NOT_SET = "The \"FileNamePattern\" property must be set before using FixedWindowRollingPolicy. ";
    static final String PRUDENT_MODE_UNSUPPORTED = "See also "+CODES_URL+"#tbr_fnp_prudent_unsupported";
    static final String SEE_PARENT_FN_NOT_SET = "Please refer to "+CODES_URL+"#fwrp_parentFileName_not_set";
    FileNamePattern fileNamePattern;
    protected String elapsedPeriodsFileName;
    private int deleteByPeriodValue =-1;
    private int deleteByFileNumberValue=Integer.MAX_VALUE;
    private int maxHistory=0;
    private String path;
    private Calendar cal = Calendar.getInstance();

    Future<?> deleteFuture;
    private String intervalUnit;
    private String deleteUnit;


    public LogbackTimeBasedRollingPolicy(String intervalUnit,String deleteByPeriodValue, String deleteByFileNumberValue){
        if(deleteByPeriodValue!=null){
            this.deleteUnit=deleteByPeriodValue.substring(deleteByPeriodValue.length()-1);
            String timeValue=deleteByPeriodValue.substring(0,deleteByPeriodValue.length()-1);
            this.deleteByPeriodValue =MyLogger.getMillisByTimeValueAndUnit(deleteUnit,timeValue);
        }
        else{
            this.deleteByFileNumberValue = Integer.parseInt(deleteByFileNumberValue);
        }
        this.intervalUnit=intervalUnit;

    }
    public void setFileNamePattern(String fnp){
        this.fileNamePatternStr=fnp;
        this.setPath();
    }
    public void setPath(){
        int lastIndex=fileNamePatternStr.lastIndexOf("/");
        this.path=fileNamePatternStr.substring(0,lastIndex+1);
    }

    public void start(){
        renameUtil.setContext(this.context);
        if (fileNamePatternStr != null) {
            fileNamePattern = new FileNamePattern(fileNamePatternStr, this.context);
        } else {
            addError(FNP_NOT_SET);
            addError(CoreConstants.SEE_FNP_NOT_SET);
            throw new IllegalStateException(FNP_NOT_SET + CoreConstants.SEE_FNP_NOT_SET);
        }
        super.start();
    }

//    public void stop(){
//        if(!isStarted()){
//            return;
//        }
//        waitForAsynchronousJobToStop(deleteFuture,"delete");
//        super.stop();
//    }

//    private void waitForAsynchronousJobToStop(Future<?> aFuture, String jobDescription){
//        if (aFuture != null) {
//            try {
//                aFuture.get(CoreConstants.SECONDS_TO_WAIT_FOR_COMPRESSION_JOBS, TimeUnit.SECONDS);
//            } catch (TimeoutException e) {
//                addError("Timeout while waiting for " + jobDescription + " job to finish", e);
//            } catch (Exception e) {
//                addError("Unexpected exception while waiting for " + jobDescription + " job to finish", e);
//            }
//        }
//    }

    @Override
    public void rollover() throws RolloverFailure {
        long now=System.currentTimeMillis();
        //////////////////////////////////////////////////////////////
        Date dateOfElapsedPeriod = new Date();
        cal.setTime(dateOfElapsedPeriod);
        dateOfElapsedPeriod=getExactFileName();
        System.out.println(dateOfElapsedPeriod);
        addInfo("Elapsed period: " + dateOfElapsedPeriod);
        elapsedPeriodsFileName = fileNamePattern.convert(dateOfElapsedPeriod);
        renameUtil.rename(getActiveFileName(),elapsedPeriodsFileName);
        //////////////////////////////////////////////////////////////
        deleteAsynchronously(now);
    }
    @Override
    public String getActiveFileName() {
        return getParentsRawFileProperty();
    }
    private void deleteAsynchronously(long now){
        DeleteRunnable runnable = new DeleteRunnable(deleteByPeriodValue, deleteByFileNumberValue,deleteUnit,fileNamePatternStr,now);
        ExecutorService executorService = context.getScheduledExecutorService();
        Future<?> future = executorService.submit(runnable);
    }
    private Date getExactFileName(){
        Date result;
        switch (intervalUnit) {
            case "d":
            case "D":
            case "day":
                cal.add(Calendar.DATE,-1);
                break;
            case "h":
            case "H":
            case "hour":
                cal.add(Calendar.HOUR,-1);
                break;
            case "m":
            case "M":
            case "minute":
                cal.add(Calendar.MINUTE,-1);
                break;
            case "s":
            case "S":
            case "second":
                cal.add(Calendar.SECOND,-1);
                break;
        }
        result=cal.getTime();

        return result;
    }

    private class DeleteRunnable implements Runnable {

        private List<File> fileList;
        private int periodValue;
        private int numberValue;
        private long now;
        private int timeIndexLength;
        DeleteRunnable(int periodValue, int numberValue,String deleteUnit, String fileNamePatternStr, long now) {
            this.periodValue = periodValue;
            this.numberValue = numberValue;
            setTimeIndexLength(fileNamePatternStr);
            this.now=now;
        }
        private void setTimeIndexLength(String fileNamePatternStr){
            int s = fileNamePatternStr.indexOf("{") + 1;
            int e= fileNamePatternStr.lastIndexOf("}");
            String sub=fileNamePatternStr.substring(s,e);
            this.timeIndexLength=e-s;
        }
        private long getFileTimeByName(String fileName) {
            System.out.println("[getFileTimeByName] "+fileName.length());
            int end=fileName.lastIndexOf(".log");
            System.out.println("================================");
            fileName=fileName.substring(end-timeIndexLength,end);
            System.out.println("fileTime: "+fileName);

            long ret=0L;
            SimpleDateFormat dateFormat = null;

            try {
                if(fileName.length()==13) {
                    dateFormat = new SimpleDateFormat("yyMMdd_HHmmss");
                } else if(fileName.length() == 11) {
                    dateFormat = new SimpleDateFormat("yyMMdd_HHmm");
                } else if(fileName.length() == 9) {
                    dateFormat = new SimpleDateFormat("yyMMdd_HH");
                } else if(fileName.length() == 6) {
                    dateFormat = new SimpleDateFormat("yyMMdd");
                }

                ret=dateFormat.parse(fileName).getTime();
                System.out.println("ret: "+ret);
            } catch (ParseException e) {
                addError(e.getMessage());
            }

            return ret;
        }
        private void periodByFileName(long now) {
            long fileTime;
            System.out.println("[periodByFileName]");
            for(File f : fileList) {
                System.out.println("fileName: "+f.getName());
                try {
                    fileTime=getFileTimeByName(f.getName());
                    System.out.println("-fileTime: "+fileTime);
                    if((now - fileTime) >= deleteByPeriodValue) {
                        Files.deleteIfExists(Paths.get(f.getPath()));
                    }
                } catch (IOException e){
                    addError(e.getMessage());
                }
            }
        }
        private void periodByLastModifiedTime(long now) {
            long fileTime;
            System.out.println("[periodByLastModifiedTime]");
            for(File f:fileList) {
                try {
                    fileTime=((FileTime) Files.getAttribute(f.toPath(),"lastModifiedTime")).toMillis();
                    System.out.println(f.getName() + " " + fileTime);
                    System.out.println((now-fileTime) + " " + deleteByPeriodValue);
                    if((now-fileTime)>= deleteByPeriodValue){
                        Files.deleteIfExists(Paths.get(f.getPath()));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        private void deleteByPeriod(long now){
            long fileTime;
            System.out.println("[periodByFileName]");
            for(File f : fileList) {
                System.out.println("fileName: "+f.getName());
                try {
                    fileTime=getFileTimeByName(f.getName());
                    System.out.println("-fileTime: "+fileTime);
                    if((now - fileTime) >= deleteByPeriodValue) {
                        Files.deleteIfExists(Paths.get(f.getPath()));
                    }
                } catch (IOException e){
                    addError(e.getMessage());
                }
            }

        }
        private void deleteByNumber(int numberValue){
            int haveToDelete=fileList.size()-numberValue;
            try{
                for(int i=0 ; i<= haveToDelete ; i++) {
                    Files.deleteIfExists(Paths.get(fileList.get(i).getPath()));
                }
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        private void getRollOveredFiles(){
            File dir=new File(path);
            String rollOveredFileNamePrefix=getActiveFileName().substring(getActiveFileName().lastIndexOf("/")+1,getActiveFileName().lastIndexOf(".log"))+"_";
            FilenameFilter filter= (dir1, name) -> name.startsWith(rollOveredFileNamePrefix)&&name.endsWith(".log");
            File[] rollOveredFileList= dir.listFiles(filter);
            Arrays.sort(rollOveredFileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    long n1=extractNumber(o1.getName());
                    long n2=extractNumber(o2.getName());
                    return (int) (n1-n2);
                }
                public long extractNumber(String name){
                    long i=0;
                    int s=name.indexOf("_")+1;
                    int e=name.lastIndexOf(".");
                    String number=name.substring(s,e);
                    if(number.contains("_")){
                        number=number.substring(0,number.indexOf("_"))+number.substring((number.indexOf("_")+1));
                    }
                    i=Long.parseLong(number);
                    return i;
                }
            });
            this.fileList=Arrays.asList(rollOveredFileList);
        }
        @Override
        public void run() {
            getRollOveredFiles();
            if(this.periodValue!=-1) {
                deleteByPeriod(now);
            } else{
                deleteByNumber(this.numberValue);
            }
        }
    }

}
