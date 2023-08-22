package dbappender_proto2.column_converter;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import dbappender_proto2.Info;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class LogbackColumnFactory implements ColumnFactory{
    private List<String> columnList = new ArrayList<>();
    private Map<String, Map<String, Integer>> patternIndexMap = new HashMap<>();
    private Map<String, Map<String, String>> columnPatternMap = new HashMap<>();
    public Map<String, String> getColumnPatternMap(String tableName) {
        return columnPatternMap.get(tableName);
    }

    public Map<String, Integer> getPatternIndexMap(String tableName) {
        return patternIndexMap.get(tableName);
    }
    @Override
    public void mappingColumn(String pattern, String tableName) {
        List<String> patterns = new ArrayList<>(Arrays.asList(pattern.split("%")));
        ColumnConverter columnConverter = Info.getInstance().columnConverter;
        Map<String, Integer> piMap = new HashMap<>();
        Map<String, String> cpMap = new HashMap<>();
//        patterns.stream()
//                .map(String::trim)
//                .collect(Collectors.toList());
        int index = 1;
        for(String p : patterns) {
            p = p.trim();
            String columnName = null;
            if (p.startsWith("c") || p.startsWith("logger") || p.startsWith("lo")) {
                columnName = columnConverter.getLoggerColumnName();
                p = "logger";
            } else if (p.startsWith("C") || p.startsWith("class")) {
                columnName = columnConverter.getClassColumnName();
                p = "class";
            } else if (p.startsWith("contextName") || p.startsWith("cn")) {
                columnName = columnConverter.getContextNameColumnName();
                p = "contextName";
            } else if (p.startsWith("d") || p.startsWith("date")) {
                columnName = columnConverter.getDateColumnName();
                String dateFormat = getDateFormat(p);
                p = "d:" + dateFormat;
            } else if (p.startsWith("F") || p.startsWith("file")) {
                columnName = columnConverter.getFileColumnName();
                p = "file";
            } else if (p.startsWith("caller")) {
                columnName = columnConverter.getCallerColumnName();
                p = "caller";
            } else if (p.startsWith("L") || p.startsWith("line")) {
                columnName = columnConverter.getLineColumnName();
                p = "line";
            } else if (p.startsWith("m") || p.startsWith("msg") || p.startsWith("message")) {
                columnName = columnConverter.getMessageColumnName();
                p = "m";
            } else if (p.startsWith("p") || p.startsWith("level") || p.startsWith("le")) {
                columnName = columnConverter.getLevelColumnName();
                p = "level";
            } else if (p.startsWith("t") || p.startsWith("thread")) {
                columnName = columnConverter.getThreadColumnName();
                p = "thread";
            } else if (p.startsWith("ex") || p.startsWith("exception") || p.startsWith("throwable")) {
                columnName = columnConverter.getExceptionColumnName();
                p = "ex";
            } else if (p.equals("")) continue;
            piMap.put(p,index++);
            cpMap.put(columnName,p);
//            ciList.add(new ColumnIndex(columnName, index++));
//            cpList.add(new ColumnPattern(columnName,p));
            columnList.add(columnName);
        }
        patternIndexMap.put(tableName,piMap);
        columnPatternMap.put(tableName,cpMap);
    }

    private String getDateFormat (String str) {
        int start = str.indexOf("{");
        int end = str.indexOf("}", start);

        String ret = str.substring(start+1, end);
        ret = ret.trim();
        return ret;
    }
}
