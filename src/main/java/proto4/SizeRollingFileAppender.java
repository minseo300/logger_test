package proto4;

import ch.qos.logback.core.rolling.RollingFileAppender;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SizeRollingFileAppender<E> extends RollingFileAppender<E> {
    private static long start = System.currentTimeMillis();
    private String deleteRollingFilePeriod=null;
    private int rollOverTimeInterval;

    private String path;
    private String fileName;

    public SizeRollingFileAppender(String path, String fileName){
        this.path=path;
        this.fileName=fileName;
    }
    public SizeRollingFileAppender(String path,String fileName,String deleteRollingFilePeriod){
        this.rollOverTimeInterval=MyLogger.getLogbackMaxHistory(deleteRollingFilePeriod);
        this.deleteRollingFilePeriod= deleteRollingFilePeriod;
        this.path=path;
        this.fileName=fileName;
    }

    @Override
    public void rollover() {
        System.out.println("rollover "+deleteRollingFilePeriod);
        super.rollover();
    }


}
