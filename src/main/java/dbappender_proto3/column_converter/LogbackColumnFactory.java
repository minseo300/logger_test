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
import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LogbackColumnFactory implements ColumnFactory {

    // 각각의 테이블은 서로 다른 column data들을 가진다; <tableName, columnMap> ; columnMap - converter, index for prepared statement
    private Map<String, List<Object>> tableQueryDataMap = new HashMap<>();
    // 각각의 테이블은 서로 다른 column name들을 가진다;
    private Map<String, List<String>> tableColumnMap = new HashMap<>();

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

        List<String> columnNameList = new ArrayList<>();
        List<Converter> converters =new ArrayList<>();

        String columnName = null;
        int index = 0;
        for (Converter c : converterList) {
            if (c instanceof DateConverter) {
                ((DateConverter) c).start();
                columnName = columnConverter.getDateColumnName();
            } else if (c instanceof RelativeTimeConverter) {
                ((RelativeTimeConverter) c).start();
                columnName = columnConverter.getDateColumnName(); // TODO
            } else if (c instanceof LevelConverter) {
                ((LevelConverter) c).start();
                columnName = columnConverter.getLevelColumnName();
            } else if (c instanceof ThreadConverter) {
                ((ThreadConverter) c).start();
                columnName = columnConverter.getThreadColumnName();
            } else if (c instanceof LoggerConverter) {
                ((LoggerConverter) c).start();
                columnName = columnConverter.getLoggerColumnName();
            } else if (c instanceof MessageConverter) {
                ((MessageConverter) c).start();
                columnName = columnConverter.getMessageColumnName();
            } else if (c instanceof ClassOfCallerConverter) {
                ((ClassOfCallerConverter) c).start();
                columnName = columnConverter.getClassColumnName();
            } else if (c instanceof MethodOfCallerConverter) {
                ((MethodOfCallerConverter) c).start();
                columnName = columnConverter.getMethodColumnName();
            } else if (c instanceof LineOfCallerConverter) {
                ((LineOfCallerConverter) c).start();
                columnName = columnConverter.getLineColumnName();
            } else if (c instanceof FileOfCallerConverter) {
                ((FileOfCallerConverter) c).start();
                columnName = columnConverter.getFileColumnName();
            } else if (c instanceof ThrowableProxyConverter) {
                ((ThrowableProxyConverter) c).start();
                columnName = columnConverter.getExceptionColumnName();
            } else if (c instanceof ExtendedThrowableProxyConverter) {
                columnName = columnConverter.getDateColumnName(); // TODO
            } else if (c instanceof NopThrowableInformationConverter) {
                ((NopThrowableInformationConverter) c).start();
                columnName = columnConverter.getDateColumnName(); // TODO
            } else if (c instanceof  ContextNameConverter) {
                ((ContextNameConverter) c).start();
                columnName = columnConverter.getContextNameColumnName();
            } else if (c instanceof CallerDataConverter) {
                ((CallerDataConverter) c).start();
                columnName = columnConverter.getCallerColumnName();
            } else if (c instanceof PropertyConverter){
                ((PropertyConverter) c).start();
                columnName = columnConverter.getDateColumnName();
            }

            columnNameList.add(index, columnName);
            converters.add(index++, c);
        }

        tableQueryDataMap.put(tableName, Collections.singletonList(converters));
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
