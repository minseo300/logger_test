package proto4;

import ch.qos.logback.core.rolling.RollingPolicyBase;
import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import ch.qos.logback.core.rolling.helper.RenameUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class LogbackSizeBasedRollingPolicy extends RollingPolicyBase {
    private int deleteByPeriodValue =-1;
    private int deleteByFileNumberValue;
    private String path;
    String fileNamePatternStr;
    static FileNamePattern fileNamePattern;
    private int minIndex;
    private int numberOfFiles=0;
    static RenameUtil renameUtil = new RenameUtil();

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

    public LogbackSizeBasedRollingPolicy(String deleteByPeriodValue, String deleteByFileNumberValue){
        if(deleteByPeriodValue!=null){
            String timeUnit=deleteByPeriodValue.substring(deleteByPeriodValue.length()-1);
            String timeValue=deleteByPeriodValue.substring(0,deleteByPeriodValue.length()-1);
            this.deleteByPeriodValue =MyLogger.getMillisByTimeValueAndUnit(timeUnit,timeValue);
        }
        else{
            this.deleteByFileNumberValue = Integer.parseInt(deleteByFileNumberValue)-1;
        }
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
        renameUtil.rename(getActiveFileName(), fileNamePattern.convertInt(minIndex++));
        numberOfFiles++;
    }
    private void deleteFilesByPeriod() throws IOException {
        List<File> rollOveredFileList=getRollOveredFiles();
        long currentTime = System.currentTimeMillis();
        long creationTime;
        for(File f:rollOveredFileList){
            creationTime=((FileTime) Files.getAttribute(f.toPath(),"creationTime")).toMillis();
            if((deleteByPeriodValue+creationTime)> currentTime){
                Files.deleteIfExists(Paths.get(f.getPath()));
            }
            else break;
        }

    }
    private void deleteFileByFileNumber() throws IOException {
        if(deleteByFileNumberValue<=numberOfFiles){
            List<File> rollOveredFileList=getRollOveredFiles();
            if(rollOveredFileList.size()>this.deleteByFileNumberValue){
                Files.deleteIfExists(Paths.get(rollOveredFileList.get(0).getPath()));
                numberOfFiles--;
            }
        }

    }

    @Override
    public String getActiveFileName() {
        return null;
    }
    private List<File> getRollOveredFiles(){
        java.io.File dir=new java.io.File(path);
        String rollOveredFileNamePrefix=getActiveFileName().substring(getActiveFileName().lastIndexOf("/")+1,getActiveFileName().lastIndexOf(".log"))+"-";
        FilenameFilter filter= (dir1, name) -> name.startsWith(rollOveredFileNamePrefix)&&name.endsWith(".log");
        java.io.File[] rollOveredFileList= dir.listFiles(filter);
        Arrays.sort(rollOveredFileList, new Comparator<java.io.File>() {
            @Override
            public int compare(java.io.File o1, java.io.File o2) {
                int n1=extractNumber(o1.getName());
                int n2=extractNumber(o2.getName());
                return n1-n2;
            }
            public int extractNumber(String name){
                int i=0;
                int s=name.indexOf("-")+1;
                int e=name.lastIndexOf(".");
                String number=name.substring(s,e);
                i=Integer.parseInt(number);
                return i;
            }
        });
        java.util.List<java.io.File> result=Arrays.asList(rollOveredFileList);

        return result;
    }
}