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
    static FileNamePattern fileNamePattern;
    protected String elapsedPeriodsFileName;
    private int deleteByPeriodValue =-1;
    private int deleteByFileNumberValue=Integer.MAX_VALUE;
    private int maxHistory=0;
    private String path;
    private Calendar cal = Calendar.getInstance();

    Future<?> deleteFuture;
    private String intervalUnit;


    public LogbackTimeBasedRollingPolicy(String intervalUnit,String deleteByPeriodValue, String deleteByFileNumberValue){
        if(deleteByPeriodValue!=null){
            String timeUnit=deleteByPeriodValue.substring(deleteByPeriodValue.length()-1);
            String timeValue=deleteByPeriodValue.substring(0,deleteByPeriodValue.length()-1);
            this.deleteByPeriodValue =MyLogger.getMillisByTimeValueAndUnit(timeUnit,timeValue);
        }
        else{
            this.deleteByFileNumberValue = Integer.parseInt(deleteByFileNumberValue)-1;
        }
        this.intervalUnit=intervalUnit;

    }
    public void setFileNamePattern(String fnp){
        this.fileNamePatternStr=fnp;
        this.setPath();
    }
//    public void setMaxHistory()
    public void setPath(){
        int lastIndex=fileNamePatternStr.lastIndexOf("/");
        this.path=fileNamePatternStr.substring(0,lastIndex+1);
        System.out.println("path: "+path);
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

        addInfo("Elapsed period: " + dateOfElapsedPeriod);
        elapsedPeriodsFileName = fileNamePattern.convert(dateOfElapsedPeriod);
        renameUtil.rename(getActiveFileName(),elapsedPeriodsFileName);
        System.out.println("SUCCESS ROLLING ACTIVE FILE");
        //////////////////////////////////////////////////////////////
        System.out.println("ROLLOVER TIME: "+(System.currentTimeMillis()-now));
        deleteAsynchronously(deleteByPeriodValue,deleteByFileNumberValue);
    }
    @Override
    public String getActiveFileName() {
        return getParentsRawFileProperty();
    }
    private void deleteAsynchronously(int deleteByPeriodValue, int deleteByFileNumberValue){
        DeleteRunnable runnable = new DeleteRunnable(deleteByPeriodValue, deleteByFileNumberValue);
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
        DeleteRunnable(int periodValue, int numberValue) {
            this.periodValue = periodValue;
            this.numberValue = numberValue;
        }
        private void deleteByPeriod(long now){
            long fileTime;
            System.out.println("[PERIOD] thread: "+Thread.currentThread());
            for(File f:fileList) {
                try {
                    fileTime=((FileTime) Files.getAttribute(f.toPath(),"lastModifiedTime")).toMillis();
                    if((now-fileTime)>= deleteByPeriodValue){
                        Files.deleteIfExists(Paths.get(f.getPath()));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        private void deleteByNumber(int numberValue){
            System.out.println("[NUMBER] thread: "+Thread.currentThread().getName());
            int haveToDelete=fileList.size()-numberValue;
            try{
                for(int i=0 ; i<haveToDelete ; i++) {
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
            long now=System.currentTimeMillis();
            getRollOveredFiles();
            if(this.periodValue!=-1) {
                deleteByPeriod(now);
            } else{
                deleteByNumber(this.numberValue);
            }
        }
    }

}
