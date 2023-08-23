package dbappender_proto3.column_converter;


import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.*;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.LiteralConverter;
import ch.qos.logback.core.pattern.parser.Node;
import ch.qos.logback.core.pattern.parser.Parser;
import ch.qos.logback.core.spi.ScanException;
import dbappender_proto3.Info;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LogbackColumnFactory implements ColumnFactory {

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
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        ColumnConverter columnConverter = Info.getInstance().columnConverter;
        PatternLayout pl = new PatternLayout();
        pl.setPattern(pattern);
        pl.setContext(context);
        pl.start();

        List<Converter> converterList = new ArrayList<>();
        try {
            Parser parser = new Parser(pattern);
            parser.setContext(context);
            Node t = parser.parse();
            Map<String, String> ecm = pl.getEffectiveConverterMap();
            Converter head = parser.compile(t,ecm);
            while (head !=null) {
                if (!(head instanceof LiteralConverter) && !(head instanceof LineSeparatorConverter)) {
                    converterList.add(head);
                }
                head = head.getNext();
            }
        } catch (ScanException e) {
            throw new RuntimeException(e);
        }

        Map<Object, Integer> columnDataMap = new HashMap<>();
        Map<String, Integer> columnNameIndexMap = new HashMap<>();
        String[] columnNameArr = new String[converterList.size()+1];
        Converter[] converterArr = new Converter[converterList.size()+1];

        String columnName = null;
        int index = 0;
        for (Converter c : converterList) {
            if (c instanceof DateConverter) {
                ((DateConverter) c).start();
                columnName = columnConverter.getDateColumnName();
                index = DATE_INDEX;
            } else if (c instanceof RelativeTimeConverter) {
                ((RelativeTimeConverter) c).start();
                columnName = columnConverter.getDateColumnName(); // TODO
                index = RELATIVE_TIME_INDEX;
            } else if (c instanceof LevelConverter) {
                ((LevelConverter) c).start();
                columnName = columnConverter.getLevelColumnName();
                index = LEVEL_INDEX;
            } else if (c instanceof ThreadConverter) {
                ((ThreadConverter) c).start();
                columnName = columnConverter.getThreadColumnName();
                index = THREAD_INDEX;
            } else if (c instanceof LoggerConverter) {
                ((LoggerConverter) c).start();
                columnName = columnConverter.getLoggerColumnName();
                index = LOGGER_INDEX;
            } else if (c instanceof MessageConverter) {
                ((MessageConverter) c).start();
                columnName = columnConverter.getMessageColumnName();
                index = MESSAGE_INDEX;
            } else if (c instanceof ClassOfCallerConverter) {
                ((ClassOfCallerConverter) c).start();
                columnName = columnConverter.getClassColumnName();
                index = CLASS_CALLER_INDEX;
            } else if (c instanceof MethodOfCallerConverter) {
                ((MethodOfCallerConverter) c).start();
                columnName = columnConverter.getMethodColumnName();
                index = METHOD_CALLER_INDEX;
            } else if (c instanceof LineOfCallerConverter) {
                ((LineOfCallerConverter) c).start();
                columnName = columnConverter.getLineColumnName();
                index = LINE_CALLER_INDEX;
            } else if (c instanceof FileOfCallerConverter) {
                ((FileOfCallerConverter) c).start();
                columnName = columnConverter.getFileColumnName();
                index = FILENAME_CALLER_INDEX;
            } else if (c instanceof ThrowableProxyConverter) {
                ((ThrowableProxyConverter) c).start();
                columnName = columnConverter.getExceptionColumnName();
                index = EXCEPTION_INDEX;
            } else if (c instanceof ExtendedThrowableProxyConverter) {
                columnName = columnConverter.getDateColumnName(); // TODO
                index = EXTENDED_EXCEPTION_INDEX;
            } else if (c instanceof NopThrowableInformationConverter) {
                ((NopThrowableInformationConverter) c).start();
                columnName = columnConverter.getDateColumnName(); // TODO
                index = NOP_EXCEPTION_INDEX;
            } else if (c instanceof  ContextNameConverter) {
                ((ContextNameConverter) c).start();
                columnName = columnConverter.getContextNameColumnName();
                index = CONTEXT_NAME_INDEX;
            } else if (c instanceof CallerDataConverter) {
                ((CallerDataConverter) c).start();
                columnName = columnConverter.getCallerColumnName();
                index = CALLER_INDEX;
            } else if (c instanceof PropertyConverter){
                ((PropertyConverter) c).start();
                columnName = columnConverter.getDateColumnName();
                index = PROPERTY_INDEX;
            }
            columnDataMap.put(c, index);
            columnNameIndexMap.put(columnName, index);
            columnNameArr[index] = columnName;
            converterArr[index] = c;
        }

        tableQueryDataMap.put(tableName, Arrays.asList(Arrays.stream(converterArr).toArray()));
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
