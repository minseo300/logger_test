package dbappender_proto3.column_converter;

import dbappender_proto3.Info;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;

import java.util.*;

public class Log4j2ColumnFactory implements ColumnFactory {
    private LoggerContext context = (LoggerContext) LogManager.getContext(false);
    private Configuration configuration = context.getConfiguration();
    @Override
    public void mappingColumn(String pattern, String tableName) {
        List<String> patterns = new ArrayList<>(Arrays.asList(pattern.split("%")));
        ColumnConverter columnConverter = Info.getInstance().columnConverter;
        Map<String, Integer> piMap = new HashMap<>();
        Map<String, String> cpMap = new HashMap<>();
        PatternLayout pl = PatternLayout.newBuilder().withPattern(pattern).build();
        PatternParser parser = PatternLayout.createPatternParser(configuration);
        List<PatternFormatter> formatters = parser.parse(pattern);

    }

    @Override
    public List<String> getColumnNameList(String tableName) {
        return null;
    }

    @Override
    public List<Object> getConverterList(String tableName) {
        return null;
    }



}
