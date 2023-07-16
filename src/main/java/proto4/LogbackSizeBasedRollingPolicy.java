package proto4;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.rolling.RollingPolicyBase;
import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import ch.qos.logback.core.rolling.helper.RenameUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ch.qos.logback.core.CoreConstants.CODES_URL;

public class LogbackSizeBasedRollingPolicy extends RollingPolicyBase {
    private int deleteByPeriodValue =-1;
    private int deleteByFileNumberValue;
    private String path;
    String fileNamePatternStr;
    static FileNamePattern fileNamePattern;
    private int minIndex=1;
    private int maxIndex=1;
    private int numberOfFiles=0;

    static RenameUtil renameUtil = new RenameUtil();
    static final String FNP_NOT_SET = "The \"FileNamePattern\" property must be set before using FixedWindowRollingPolicy. ";
    static final String PRUDENT_MODE_UNSUPPORTED = "See also "+CODES_URL+"#tbr_fnp_prudent_unsupported";
    static final String SEE_PARENT_FN_NOT_SET = "Please refer to "+CODES_URL+"#fwrp_parentFileName_not_set";


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
        super.start();
    }
    public void setFileNamePattern(String fnp){
        this.fileNamePatternStr=fnp;
        this.setPath();
    }
    public void setPath(){
        int lastIndex=fileNamePatternStr.lastIndexOf("/");
        this.path=fileNamePatternStr.substring(0,lastIndex+1);
        System.out.println("path: "+path);
        try {
            setIndex();
        }catch (Exception e){
            System.out.println(e);
        }
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
        renameUtil.rename(getActiveFileName(), fileNamePattern.convertInt(maxIndex++));
//        updateIndexToFile();
        numberOfFiles++;
    }
    private void deleteFilesByPeriod() throws IOException {
        List<File> rollOveredFileList=getRollOveredFiles();
        long currentTime = System.currentTimeMillis();
        long creationTime;
        for(File f:rollOveredFileList){
            creationTime=((FileTime) Files.getAttribute(f.toPath(),"creationTime")).toMillis();
            if((currentTime-creationTime)>= deleteByPeriodValue){
                Files.deleteIfExists(Paths.get(f.getPath()));
                minIndex= Integer.parseInt(f.getName().substring(f.getName().lastIndexOf("-")));
                System.out.println(minIndex);
            }
            else break;
        }
    }

    private void deleteFileByFileNumber() throws IOException {
        List<File> rollOveredFileList=getRollOveredFiles();
        if(rollOveredFileList.size()>=this.deleteByFileNumberValue){
            Files.deleteIfExists(Paths.get(rollOveredFileList.get(0).getPath()));
            minIndex++;
        }
    }

//    private void updateIndexToFile() {
//        try{
//            Files.deleteIfExists(Paths.get(path + "/index.txt"));
//            Files.write(Paths.get(path + "/index.txt"),(minIndex+"\n"+maxIndex).getBytes(),StandardOpenOption.CREATE);
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }

    private void setIndex() throws IOException {
        ///////// V1 /////////////////////////////////////////////////////////////////
        // - minIndex와 maxIndex를 index 파일을 읽어 setting
//        Path indexFilePath = Paths.get(path+"/index.txt");
//        File indexFile=new File(indexFilePath.toString());
//
//        if(indexFile.exists()){
//            List<String> lines = Files.readAllLines(indexFilePath);
//            System.out.println(lines);
//            this.minIndex=Integer.parseInt(lines.get(0));
//            this.maxIndex=Integer.parseInt(lines.get(1));
//        }
//        else{
//            indexFilePath = Files.createFile(indexFilePath);
//            Files.write(indexFilePath, "1\n1".getBytes(),StandardOpenOption.CREATE);
//            this.minIndex=1;
//            this.maxIndex=1;
//        }
        //////////////////////////////////////////////////////////////////////////////

        ///////// V2 /////////////////////////////////////////////////////////////////
        // - directory 내에 있는 모든 파일 중 인덱스가 제일 작은 숫자와 제일 큰 숫자를 읽어 setting -> directory 내의 모든 파일 이름을 순회
        File dir=new File(path);
        String rollOveredFileNamePrefix=getActiveFileName().substring(getActiveFileName().lastIndexOf("/")+1,getActiveFileName().lastIndexOf(".log"))+"-";
        FilenameFilter filter= (dir1, name) -> name.startsWith(rollOveredFileNamePrefix)&&name.endsWith(".log");
        File[] rollOveredFileList= dir.listFiles(filter);
        int minIndex=1,maxIndex=1;
        int index;
        for(File f: rollOveredFileList){
            int s=f.getName().lastIndexOf("-")+1;
            int e=f.getName().lastIndexOf(".log");
            index= Integer.parseInt(f.getName().substring(s,e));
            System.out.println("index: "+index);
            if(minIndex==1||minIndex>index) minIndex=index;
            if(maxIndex==1||maxIndex<index) maxIndex=index;
        }
        //////////////////////////////////////////////////////////////////////////////
//        System.out.println("minIndex: "+minIndex+" maxIndex: "+maxIndex);
        this.minIndex=minIndex;
        this.maxIndex=maxIndex;
    }
    @Override
    public String getActiveFileName() {
        return getParentsRawFileProperty();
    }
    private List<File> getRollOveredFiles(){
        File dir=new File(path);
        String rollOveredFileNamePrefix=getActiveFileName().substring(getActiveFileName().lastIndexOf("/")+1,getActiveFileName().lastIndexOf(".log"))+"-";
        FilenameFilter filter= (dir1, name) -> name.startsWith(rollOveredFileNamePrefix)&&name.endsWith(".log");
        File[] rollOveredFileList= dir.listFiles(filter);
        Arrays.sort(rollOveredFileList, new Comparator<java.io.File>() {
            @Override
            public int compare(File o1, File o2) {
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
        List<File> result=Arrays.asList(rollOveredFileList);

        return result;
    }
}
