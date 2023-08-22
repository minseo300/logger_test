package dbappender_proto2.column_converter;

import java.util.Map;

public interface ColumnFactory {
    void mappingColumn(String pattern, String tableName);
    Map<String, String> getColumnPatternMap(String tableName);
    Map<String, Integer> getPatternIndexMap(String tableName);
}
