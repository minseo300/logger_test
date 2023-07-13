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
import java.util.concurrent.Future;

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
    private int deleteByFileNumberValue;
    private int maxHistory=0;
    private String path;
    protected ArchiveRemover archiveRemover = null;
    protected RollingCalendar rc;
    boolean cleanHistoryOnStart = false;
    Future<?> cleanUpFuture;

    private void setArchiveRemover(){
        DateTokenConverter<Object> dtc = fileNamePattern.getPrimaryDateTokenConverter();
        if (dtc == null) {
            throw new IllegalStateException("FileNamePattern [" + fileNamePattern.getPattern() + "] does not contain a valid DateToken");
        }

        if (dtc.getTimeZone() != null) {
            rc = new RollingCalendar(dtc.getDatePattern(), dtc.getTimeZone(), Locale.getDefault());
        } else {
            rc = new RollingCalendar(dtc.getDatePattern());
        }
        archiveRemover=new TimeBasedArchiveRemover(fileNamePattern,rc);
    }
    public LogbackTimeBasedRollingPolicy(String deleteByPeriodValue, String deleteByFileNumberValue){
        if(deleteByPeriodValue!=null){
            String timeUnit=deleteByPeriodValue.substring(deleteByPeriodValue.length()-1);
            String timeValue=deleteByPeriodValue.substring(0,deleteByPeriodValue.length()-1);
            this.deleteByPeriodValue =MyLogger.getMillisByTimeValueAndUnit(timeUnit,timeValue);
        }
        else{
            this.deleteByFileNumberValue = Integer.parseInt(deleteByFileNumberValue)-1;
        }

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
//            determineCompressionMode();
        } else {
            addError(FNP_NOT_SET);
            addError(CoreConstants.SEE_FNP_NOT_SET);
            throw new IllegalStateException(FNP_NOT_SET + CoreConstants.SEE_FNP_NOT_SET);
        }
//        setArchiveRemover();

//        if (maxHistory != UNBOUND_HISTORY) {
//            this.archiveRemover.setMaxHistory(maxHistory);
////            archiveRemover.setTotalSizeCap(totalSizeCap.getSize());
//            if (cleanHistoryOnStart) {
//                addInfo("Cleaning on start up");
//                Date now = new Date(LogbackTimeBasedTriggeringPolicy.getCurrentTime());
//                cleanUpFuture = archiveRemover.cleanAsynchronously(now);
//            }
//        }
//        else if (!isUnboundedTotalSizeCap()) {
//            addWarn("'maxHistory' is not set, ignoring 'totalSizeCap' option with value ["+totalSizeCap+"]");
//        }

//        compressor = new Compressor(compressionMode);
//        compressor.setContext(this.context);
        super.start();
    }

    @Override
    public void rollover() throws RolloverFailure {

        try{
            if(deleteByPeriodValue!=-1){
                deleteFilesByPeriod();
            }
            else{
                deleteFileByFileNumber();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        ///////////////////////////////////////////////////
        Date dateOfElapsedPeriod = new Date();
        addInfo("Elapsed period: " + dateOfElapsedPeriod);
        elapsedPeriodsFileName = fileNamePattern.convert(dateOfElapsedPeriod);
        renameUtil.rename(getActiveFileName(),elapsedPeriodsFileName);
        //////////////////////////////////////////////////////////////

    }

    private List<File> getRollOveredFiles(){
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
        List<File> result=Arrays.asList(rollOveredFileList);

        return result;
    }

    private void deleteFilesByPeriod() throws IOException {
        List<File> rollOveredFileList=getRollOveredFiles();
        long currentTime = System.currentTimeMillis();
        long creationTime;
        for(File f:rollOveredFileList){
            creationTime=((FileTime) Files.getAttribute(f.toPath(),"creationTime")).toMillis();
            if((currentTime-creationTime)>= deleteByPeriodValue){
                Files.deleteIfExists(Paths.get(f.getPath()));
            }
        }
    }
    private void deleteFileByFileNumber() throws IOException {
        List<File> rollOveredFileList=getRollOveredFiles();
        if(rollOveredFileList.size()>=this.deleteByFileNumberValue){
            Files.deleteIfExists(Paths.get(rollOveredFileList.get(0).getPath()));
        }
    }

    @Override
    public String getActiveFileName() {
        return getParentsRawFileProperty();
    }
}
