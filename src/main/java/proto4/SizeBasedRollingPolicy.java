/////**
//// * Logback: the reliable, generic, fast and flexible logging framework.
//// * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
//// *
//// * This program and the accompanying materials are dual-licensed under
//// * either the terms of the Eclipse Public License v1.0 as published by
//// * the Eclipse Foundation
//// *
//// *   or (per the licensee's choosing)
//// *
//// * under the terms of the GNU Lesser General Public License version 2.1
//// * as published by the Free Software Foundation.
//// */
////package proto4;
////
////import static ch.qos.logback.core.CoreConstants.CODES_URL;
////
////import java.io.File;
////import java.io.IOException;
////import java.nio.file.Files;
////import java.nio.file.Paths;
////import java.nio.file.attribute.FileTime;
////
////import ch.qos.logback.core.CoreConstants;
////import ch.qos.logback.core.FileAppender;
////import ch.qos.logback.core.rolling.RollingPolicyBase;
////import ch.qos.logback.core.rolling.RolloverFailure;
////import ch.qos.logback.core.rolling.helper.*;
////
/////**
//// * When rolling over, <code>FixedWindowRollingPolicy</code> renames files
//// * according to a fixed window algorithm.
//// *
//// * For more information about this policy, please refer to the online manual at
//// * http://logback.qos.ch/manual/appenders.html#FixedWindowRollingPolicy
//// *
//// * @author Ceki G&uuml;lc&uuml;
//// */
////public class SizeBasedRollingPolicy extends RollingPolicyBase {
////    static final String FNP_NOT_SET = "The \"FileNamePattern\" property must be set before using FixedWindowRollingPolicy. ";
////    static final String PRUDENT_MODE_UNSUPPORTED = "See also "+CODES_URL+"#tbr_fnp_prudent_unsupported";
////    static final String SEE_PARENT_FN_NOT_SET = "Please refer to "+CODES_URL+"#fwrp_parentFileName_not_set";
////    int maxIndex;
////    int minIndex;
////    static RenameUtil util = new RenameUtil();
////    Compressor compressor;
////
////    protected CompressionMode compressionMode = CompressionMode.NONE;
////
////    static FileNamePattern fileNamePattern;
////    // fileNamePatternStr is always slashified, see setter
////    String fileNamePatternStr;
////
////    private FileAppender<?> parent;
////
////    // use to name files within zip file, i.e. the zipEntry
////    FileNamePattern zipEntryFileNamePattern;
////    private boolean started;
////    private int numberOfFiles=0;
////    private String deleteRollingFilePeriod;
////
////    public static final String ZIP_ENTRY_DATE_PATTERN = "yyyy-MM-dd_HHmm";
////
////    /**
////     * It's almost always a bad idea to have a large window size, say over 20.
////     */
////    private static int MAX_WINDOW_SIZE = 1000000;
////
////    public SizeBasedRollingPolicy() {
////        minIndex = 1;
////        maxIndex = 1000000;
////    }
////
////    public void start() {
////        util.setContext(this.context);
////
////        if (fileNamePatternStr != null) {
////            fileNamePattern = new FileNamePattern(fileNamePatternStr, this.context);
//////            determineCompressionMode();
////        } else {
////            addError(FNP_NOT_SET);
////            addError(CoreConstants.SEE_FNP_NOT_SET);
////            System.out.println("fileNamePatternStr: "+fileNamePatternStr);
////            throw new IllegalStateException(FNP_NOT_SET + CoreConstants.SEE_FNP_NOT_SET);
////        }
////
////        if (isParentPrudent()) {
////            addError("Prudent mode is not supported with FixedWindowRollingPolicy.");
////            addError(PRUDENT_MODE_UNSUPPORTED);
////            throw new IllegalStateException("Prudent mode is not supported.");
////        }
////
////        if (getParentsRawFileProperty() == null) {
////            addError("The File name property must be set before using this rolling policy.");
////            addError(SEE_PARENT_FN_NOT_SET);
////            throw new IllegalStateException("The \"File\" option must be set.");
////        }
////
////        if (maxIndex < minIndex) {
////            addWarn("MaxIndex (" + maxIndex + ") cannot be smaller than MinIndex (" + minIndex + ").");
////            addWarn("Setting maxIndex to equal minIndex.");
////            maxIndex = minIndex;
////        }
////
////        final int maxWindowSize = getMaxWindowSize();
////        if ((maxIndex - minIndex) > maxWindowSize) {
////            addWarn("Large window sizes are not allowed.");
////            maxIndex = minIndex + maxWindowSize;
////            addWarn("MaxIndex reduced to " + maxIndex);
////        }
////
////        IntegerTokenConverter itc = fileNamePattern.getIntegerTokenConverter();
////
////        if (itc == null) {
////            throw new IllegalStateException("FileNamePattern [" + fileNamePattern.getPattern() + "] does not contain a valid IntegerToken");
////        }
////
////        if (compressionMode == CompressionMode.ZIP) {
////            String zipEntryFileNamePatternStr = transformFileNamePatternFromInt2Date(fileNamePatternStr);
////            zipEntryFileNamePattern = new FileNamePattern(zipEntryFileNamePatternStr, context);
////        }
////        compressor = new Compressor(compressionMode);
////        compressor.setContext(this.context);
////        super.start();
////    }
////
////    /**
////     * Subclasses can override this method to increase the max window size, if required.  This is to
////     * address LOGBACK-266.
////     * @return
////     */
////    protected int getMaxWindowSize() {
////        return MAX_WINDOW_SIZE;
////    }
////
////    private String transformFileNamePatternFromInt2Date(String fileNamePatternStr) {
////        String slashified = FileFilterUtil.slashify(fileNamePatternStr);
////        String stemOfFileNamePattern = FileFilterUtil.afterLastSlash(slashified);
////        return stemOfFileNamePattern.replace("%i", "%d{" + ZIP_ENTRY_DATE_PATTERN + "}");
////    }
////
////    public void rollover() throws RolloverFailure {
////        // delete the oldest log file if the number of files is over than limitRollingFileNumber(maxIndex)
////        if(maxIndex<1000000){
////            System.out.println("limitRollOver");
////            limitRollover();
////        }
////        else{
////            try {
////                System.out.println("deleteRollOver");
////                deleteRollover();
////            } catch (IOException e) {
////                throw new RuntimeException(e);
////            }
////        }
////
////        // rollover active file(fileName.log) by triggering event
////        util.rename(getActiveFileName(), fileNamePattern.convertInt(numberOfFiles+1));
////        numberOfFiles++;
////        System.out.println(numberOfFiles);
////    }
////    public void limitRollover(){
////        if(maxIndex<=numberOfFiles){
////            File file = new File(fileNamePattern.convertInt(minIndex));
////            if (file.exists()) {
////                file.delete();
////                numberOfFiles--;
////            }
////            for(int i=minIndex+1;i<=maxIndex;i++){
////                String toRenameStr = fileNamePattern.convertInt(i);
////                File toRename = new File(toRenameStr);
////                // no point in trying to rename an inexistent file
////                if (toRename.exists()) {
////                    util.rename(toRenameStr, fileNamePattern.convertInt(i - 1));
////                } else {
////                    addInfo("Skipping roll-over for inexistent file " + toRenameStr);
////                }
////            }
////        }
////    }
////    public void deleteRollover() throws IOException {
////        System.out.println("deleteRollingFilePeriod: "+deleteRollingFilePeriod);
////        long currentTime = System.currentTimeMillis();
////        System.out.println(currentTime);
////        int converted=MyLogger.getLogbackMaxHistory(deleteRollingFilePeriod);
////        System.out.println(converted);
////        int maxIntervalSinceLastLoggingInMillis = MyLogger.getLogbackMaxHistory(deleteRollingFilePeriod) * 1000;
////        System.out.println(maxIntervalSinceLastLoggingInMillis);
////        int deletedFileNumber=0,maxIndex=numberOfFiles;
////        for (int i = minIndex; i <= maxIndex; i++) {
////            File file = new File(fileNamePattern.convertInt(i));
////            System.out.println(file.getPath());
////            FileTime creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
////            long createdTime = creationTime.toMillis();
////            // 결과 출력
//////            System.out.println(creationTime);
////            if ((currentTime - createdTime) >= maxIntervalSinceLastLoggingInMillis) {
////                System.out.println(creationTime);
////                Files.deleteIfExists(Paths.get(file.getPath()));
////                deletedFileNumber++;
////                numberOfFiles--;
////            }
////            else break;
////        }
////        if(deletedFileNumber>0) {
////            for(int i=minIndex+deletedFileNumber;i<=maxIndex;i++){
////                String toRenameStr = fileNamePattern.convertInt(i);
////                File toRename = new File(toRenameStr);
////                // no point in trying to rename an inexistent file
////                if (toRename.exists()) {
////                    util.rename(toRenameStr, fileNamePattern.convertInt(i - deletedFileNumber));
////                } else {
////                    addInfo("Skipping roll-over for inexistent file " + toRenameStr);
////                }
////            }
////        }
////        System.out.println("deleteRollOver: "+numberOfFiles);
////    }
////
////
////
////
////    /**
////     * Return the value of the parent's RawFile property.
////     */
////    public String getActiveFileName() {
////        return getParentsRawFileProperty();
////    }
////
////    public int getMaxIndex() {
////        return maxIndex;
////    }
////
////    public int getMinIndex() {
////        return minIndex;
////    }
////
////    public void setMaxIndex(int maxIndex) {
////        this.maxIndex = maxIndex;
////    }
////    public void setFileNamePattern(String fileNamePattern){
////        this.fileNamePatternStr=fileNamePattern;
////    }
////
////    public void setMinIndex(int minIndex) {
////        this.minIndex = minIndex;
////    }
////    public void setDeleteRollingFilePeriod(String deleteRollingFilePeriod){
////        this.deleteRollingFilePeriod=deleteRollingFilePeriod;
////    }
////}
//
//
//package com.tmax.proobject.runtime.logger;
//
//import ch.qos.logback.core.CoreConstants;
//import ch.qos.logback.core.FileAppender;
//import ch.qos.logback.core.rolling.RollingPolicyBase;
//import ch.qos.logback.core.rolling.RolloverFailure;
//import ch.qos.logback.core.rolling.helper.*;
//
//import java.io.File;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.attribute.FileTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import static ch.qos.logback.core.CoreConstants.CODES_URL;
//
//public class SizeBasedRollingPolicy extends RollingPolicyBase {
//    static final String FNP_NOT_SET = "The \"FileNamePattern\" property must be set before using FixedWindowRollingPolicy. ";
//    static final String PRUDENT_MODE_UNSUPPORTED = "See also "+CODES_URL+"#tbr_fnp_prudent_unsupported";
//    static final String SEE_PARENT_FN_NOT_SET = "Please refer to "+CODES_URL+"#fwrp_parentFileName_not_set";
//    int maxIndex;
//    int minIndex;
//    static RenameUtil util = new RenameUtil();
//    Compressor compressor;
//
//    protected CompressionMode compressionMode = CompressionMode.NONE;
//
//    private FileNamePattern fileNamePattern;
//    // fileNamePatternStr is always slashified, see setter
//    String fileNamePatternStr;
//
//    private FileAppender<?> parent;
//
//    // use to name files within zip file, i.e. the zipEntry
//    FileNamePattern zipEntryFileNamePattern;
//    private boolean started;
//    private int numberOfFiles=0;
//    private String deleteRollingFilePeriod;
//
//    public static final String ZIP_ENTRY_DATE_PATTERN = "yyyy-MM-dd_HHmm";
//
//    /**
//     * It's almost always a bad idea to have a large window size, say over 20.
//     */
//    private static int MAX_WINDOW_SIZE = 1000000;
//    private String path;
//    private String fileName;
//
//    public SizeBasedRollingPolicy(String path, String fileName) {
//        minIndex = 1;
//        maxIndex = 1000000;
//        this.path=path;
//        this.fileName=fileName;
//    }
//
//    public void start() {
//        util.setContext(this.context);
//
//        if (fileNamePatternStr != null) {
//            fileNamePattern = new FileNamePattern(fileNamePatternStr, this.context);
////            determineCompressionMode();
//        } else {
//            addError(FNP_NOT_SET);
//            addError(CoreConstants.SEE_FNP_NOT_SET);
//            System.out.println("fileNamePatternStr: "+fileNamePatternStr);
//            throw new IllegalStateException(FNP_NOT_SET + CoreConstants.SEE_FNP_NOT_SET);
//        }
//
//        if (isParentPrudent()) {
//            addError("Prudent mode is not supported with FixedWindowRollingPolicy.");
//            addError(PRUDENT_MODE_UNSUPPORTED);
//            throw new IllegalStateException("Prudent mode is not supported.");
//        }
//
//        if (getParentsRawFileProperty() == null) {
//            addError("The File name property must be set before using this rolling policy.");
//            addError(SEE_PARENT_FN_NOT_SET);
//            throw new IllegalStateException("The \"File\" option must be set.");
//        }
//
//        if (maxIndex < minIndex) {
//            addWarn("MaxIndex (" + maxIndex + ") cannot be smaller than MinIndex (" + minIndex + ").");
//            addWarn("Setting maxIndex to equal minIndex.");
//            maxIndex = minIndex;
//        }
//
//        final int maxWindowSize = getMaxWindowSize();
//        if ((maxIndex - minIndex) > maxWindowSize) {
//            addWarn("Large window sizes are not allowed.");
//            maxIndex = minIndex + maxWindowSize;
//            addWarn("MaxIndex reduced to " + maxIndex);
//        }
//
//        IntegerTokenConverter itc = fileNamePattern.getIntegerTokenConverter();
//
//        if (itc == null) {
//            throw new IllegalStateException("FileNamePattern [" + fileNamePattern.getPattern() + "] does not contain a valid IntegerToken");
//        }
//
//        if (compressionMode == CompressionMode.ZIP) {
//            String zipEntryFileNamePatternStr = transformFileNamePatternFromInt2Date(fileNamePatternStr);
//            zipEntryFileNamePattern = new FileNamePattern(zipEntryFileNamePatternStr, context);
//        }
//        compressor = new Compressor(compressionMode);
//        compressor.setContext(this.context);
//        super.start();
//    }
//
//    /**
//     * Subclasses can override this method to increase the max window size, if required.  This is to
//     * address LOGBACK-266.
//     * @return
//     */
//    protected int getMaxWindowSize() {
//        return MAX_WINDOW_SIZE;
//    }
//
//    private String transformFileNamePatternFromInt2Date(String fileNamePatternStr) {
//        String slashified = FileFilterUtil.slashify(fileNamePatternStr);
//        String stemOfFileNamePattern = FileFilterUtil.afterLastSlash(slashified);
//        return stemOfFileNamePattern.replace("%i", "%d{" + ZIP_ENTRY_DATE_PATTERN + "}");
//    }
//
//    public void rollover() throws RolloverFailure {
//        System.out.println("rolling policy present numberOfFiles: "+numberOfFiles);
//        // delete the oldest log file if the number of files is over than limitRollingFileNumber(maxIndex)
//        if(maxIndex<1000000){
//            System.out.println("limitRollOver");
//            try {
//                limitRollover();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        else{
//            try {
//                System.out.println("deleteRollOver");
//                deleteRollover();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        // rollover active file(fileName.log) by triggering event
//        util.rename(getActiveFileName(), fileNamePattern.convertInt(minIndex++));
//        numberOfFiles++;
//        System.out.println(numberOfFiles);
//    }
//    public void limitRollover() throws IOException {
//        if(maxIndex<=numberOfFiles){
//            File dir = new File(this.path);
//            FilenameFilter filter = new FilenameFilter() {
//                public boolean accept(File f, String name) {
//                    return name.startsWith(fileName+"-")&&name.endsWith(".log");
//                }
//            };
//            File[] files= dir.listFiles(filter);
//            Arrays.sort(files, new Comparator<File>() {
//                @Override
//                public int compare(File o1, File o2) {
//                    int n1 = extractNumber(o1.getName());
//                    int n2 = extractNumber(o2.getName());
//                    return n1 - n2;
//                }
//
//                private int extractNumber(String name) {
//                    int i = 0;
//                    try {
//                        int s = name.indexOf('-')+1;
//                        int e = name.lastIndexOf('.');
//                        String number = name.substring(s, e);
//                        i = Integer.parseInt(number);
//                    } catch(Exception e) {
//                        i = 0; // if filename does not match the format
//                        // then default to 0
//                    }
//                    return i;
//                }
//            });
//            Files.deleteIfExists(Paths.get(files[0].getPath()));
//            numberOfFiles--;
//
//        }
//    }
//    public void deleteRollover() throws IOException {
//        long currentTime = System.currentTimeMillis();
//        int converted=Logback.getMaxHistory(deleteRollingFilePeriod);
//        System.out.println(converted);
//        int maxIntervalSinceLastLoggingInMillis = Logback.getMaxHistory(deleteRollingFilePeriod) * 1000;
//        System.out.println(maxIntervalSinceLastLoggingInMillis);
//        int deletedFileNumber=0,maxIndex=numberOfFiles;
//        File dir = new File(path);
//        FilenameFilter filter = new FilenameFilter() {
//            public boolean accept(File f, String name) {
//                return name.startsWith(fileName+"-")&&name.endsWith(".log");
//            }
//        };
//        List<File> files= Arrays.asList(dir.listFiles(filter));
//        Collections.sort(files);
//        for(File f: files){
//            FileTime creationTime = (FileTime) Files.getAttribute(f.toPath(), "creationTime");
//            long createdTime = creationTime.toMillis();
//
//            if ((currentTime - createdTime) >= maxIntervalSinceLastLoggingInMillis) {
//                System.out.println(creationTime);
//                Files.deleteIfExists(Paths.get(f.getPath()));
//                deletedFileNumber++;
//                numberOfFiles--;
//            }
//            else break;
//        }
//    }
//
//
//
//
//    /**
//     * Return the value of the parent's RawFile property.
//     */
//    public String getActiveFileName() {
//        return getParentsRawFileProperty();
//    }
//
//    public int getMaxIndex() {
//        return maxIndex;
//    }
//
//    public int getMinIndex() {
//        return minIndex;
//    }
//
//    public void setMaxIndex(int maxIndex) {
//        this.maxIndex = maxIndex;
//    }
//    public void setFileNamePattern(String fileNamePattern){
//        this.fileNamePatternStr=fileNamePattern;
//    }
//
//    public void setMinIndex(int minIndex) {
//        this.minIndex = minIndex;
//    }
//    public void setDeleteRollingFilePeriod(String deleteRollingFilePeriod){
//        this.deleteRollingFilePeriod=deleteRollingFilePeriod;
//    }
//}