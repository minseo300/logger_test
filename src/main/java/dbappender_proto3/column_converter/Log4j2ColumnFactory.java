package dbappender_proto3.column_converter;

import ch.qos.logback.core.pattern.Converter;
import dbappender_proto3.Info;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.*;

import java.util.*;
import java.util.stream.Collectors;

public class Log4j2ColumnFactory implements ColumnFactory {
    private LoggerContext context = (LoggerContext) LogManager.getContext(false);
    private Configuration configuration = context.getConfiguration();

    // 각각의 테이블은 서로 다른 column data들을 가진다; <tableName, columnMap> ; columnMap - converter, index for prepared statement
    private Map<String, List<Object>> tableQueryDataMap = new HashMap<>();
    // 각각의 테이블은 서로 다른 column name들을 가진다;
    private Map<String, List<String>> tableColumnMap = new HashMap<>();

    @Override
    public void mappingColumn(String pattern, String tableName) {
        ColumnConverter columnConverter = Info.getInstance().columnConverter;
        PatternLayout pl = PatternLayout.newBuilder().withPattern(pattern).build();
        PatternParser parser = PatternLayout.createPatternParser(configuration);
        List<PatternFormatter> formatters = parser.parse(pattern);

        List<LogEventPatternConverter> converterList = new ArrayList<>();
        for (PatternFormatter patternFormatter : formatters) {
            LogEventPatternConverter converter = patternFormatter.getConverter();
            String converterName = converter.getName();
            if (!converterName.equals("SimpleLiteral")){
                converterList.add(converter);
            }
        }

        List<String> columnNameList = new ArrayList<>();
        List<ColumnConfig> columnConfigList = new ArrayList<>();

        String columnName = null;
        int index = 0;
        for (LogEventPatternConverter converter : converterList) {
            ColumnConfig.Builder columnBuilder = ColumnConfig.newBuilder();
            String p = "%";
            if (converter.getName().equals("Logger")) {
                columnName = columnConverter.getLoggerColumnName();
                p += "logger";
            } else if (converter.getName().equals("Class Name")) {
                columnName = columnConverter.getClassColumnName();
                p +="class";
            } else if (converter.getName().equals("Date")) {
                columnName = columnConverter.getDateColumnName();
//                columnBuilder.setName(columnName).setEventTimestamp(true).build();
//                columnConfigArr[index] = columnBuilder.build();
//                columnNameArr[index] = columnName;
//                continue;
                p += "date";
            } else if (converter.getName().equals("Throwable")) {
                columnName = columnConverter.getExceptionColumnName();
//                index = 1; // TODO
                p += "throwable";
            } else if (converter.getName().equals("File Location")) {
                columnName = columnConverter.getFileColumnName();
                p += "file";
            } else if (converter.getName().equals("Line")) {
                columnName = columnConverter.getLineColumnName();
                p += "line";
            } else if (converter.getName().equals("Message")) {
                columnName = columnConverter.getMessageColumnName();
                p += "message";
            } else if (converter.getName().equals("Method")) {
                columnName = columnConverter.getMethodColumnName();
                p += "method";
            } else if (converter.getName().equals("Level")) {
                columnName = columnConverter.getLevelColumnName();
                p += "level";
            } else if (converter.getName().equals("Time")) {
                columnName = columnConverter.getDateColumnName(); // TODO
            } else if (converter.getName().equals("Thread")) {
                columnName = columnConverter.getThreadColumnName();
                p += "thread";
            }
            columnBuilder.setName(columnName).setPattern(p).build();

            columnConfigList.add(index, columnBuilder.build());
            columnNameList.add(index++, columnName);
        }

        tableQueryDataMap.put(tableName, Collections.singletonList(columnConfigList));
        tableColumnMap.put(tableName, columnNameList);
    }

    @Override
    public List<String> getColumnNameList(String tableName) {
        return tableColumnMap.get(tableName);
    }

    @Override
    public List<Object> getConverterList(String tableName) {
        return tableQueryDataMap.get(tableName);
    }







}
