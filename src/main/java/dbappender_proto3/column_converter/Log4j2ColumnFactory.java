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

public class Log4j2ColumnFactory implements ColumnFactory {
    private LoggerContext context = (LoggerContext) LogManager.getContext(false);
    private Configuration configuration = context.getConfiguration();

    // 각각의 테이블은 서로 다른 column data들을 가진다; <tableName, columnMap> ; columnMap - converter, index for prepared statement
    private Map<String, List<Object>> tableQueryDataMap = new HashMap<>();
    // 각각의 테이블은 서로 다른 column name들을 가진다;
    private Map<String, List<String>> tableColumnMap = new HashMap<>();

    static final int DATE_INDEX = 1;
    static final int MESSAGE_INDEX = 2;
    static final int LOGGER_INDEX = 3;
    static final int LEVEL_INDEX = 4;
    static final int THREAD_INDEX = 5;
    static final int FILENAME_CALLER_INDEX = 6;
    static final int CLASS_CALLER_INDEX = 7;
    static final int METHOD_CALLER_INDEX = 8;
    static final int LINE_CALLER_INDEX = 9;
    static final int RELATIVE_TIME_INDEX = 10;
    static final int EXCEPTION_INDEX = 11;
    static final int EXTENDED_EXCEPTION_INDEX = 12;
    static final int NOP_EXCEPTION_INDEX = 13;
    static final int CONTEXT_NAME_INDEX = 14;
    static final int CALLER_INDEX = 15;
    static final int PROPERTY_INDEX = 16;

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

        String[] columnNameArr = new String[converterList.size()+1];
        ColumnConfig[] columnConfigArr = new ColumnConfig[converterList.size()+1];

        String columnName = null;
        int index = 0;
        for (LogEventPatternConverter converter : converterList) {
            ColumnConfig.Builder columnBuilder = ColumnConfig.newBuilder();
            String p = "%";
            if (converter.getName().equals("Logger")) {
                columnName = columnConverter.getLoggerColumnName();
                index = LOGGER_INDEX;
                p += "logger";
            } else if (converter.getName().equals("Class Name")) {
                columnName = columnConverter.getClassColumnName();
                index = CLASS_CALLER_INDEX;
                p +="class";
            } else if (converter.getName().equals("Date")) {
                columnName = columnConverter.getDateColumnName();
                index = DATE_INDEX;
                columnBuilder.setName(columnName).setEventTimestamp(true).build();
                columnConfigArr[index] = columnBuilder.build();
                columnNameArr[index] = columnName;
                continue;
            } else if (converter.getName().equals("Throwable")) {
                columnName = columnConverter.getExceptionColumnName();
                index = EXCEPTION_INDEX;
                p += "throwable";
            } else if (converter.getName().equals("File Location")) {
                columnName = columnConverter.getFileColumnName();
                index = FILENAME_CALLER_INDEX;
                p += "file";
            } else if (converter.getName().equals("Line")) {
                columnName = columnConverter.getLineColumnName();
                index = LINE_CALLER_INDEX;
                p += "line";
            } else if (converter.getName().equals("Message")) {
                columnName = columnConverter.getMessageColumnName();
                index = MESSAGE_INDEX;
                p += "message";
            } else if (converter.getName().equals("Method")) {
                columnName = columnConverter.getMethodColumnName();
                index = METHOD_CALLER_INDEX;
                p += "method";
            } else if (converter.getName().equals("Level")) {
                columnName = columnConverter.getLevelColumnName();
                index = LEVEL_INDEX;
                p += "level";
            } else if (converter.getName().equals("Time")) {
                columnName = columnConverter.getDateColumnName(); // TODO
                index = RELATIVE_TIME_INDEX;
            } else if (converter.getName().equals("Thread")) {
                columnName = columnConverter.getThreadColumnName();
                index = THREAD_INDEX;
                p += "thread";
            }
            columnBuilder.setName(columnName).setPattern(p).build();
            columnConfigArr[index] = columnBuilder.build();
            columnNameArr[index] = columnName;
        }
        tableQueryDataMap.put(tableName, Arrays.asList(Arrays.stream(columnConfigArr).toArray()));
        tableColumnMap.put(tableName, Arrays.asList(columnNameArr));
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
