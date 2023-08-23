package dbappender_proto3.column_converter;

import java.util.List;
import java.util.Map;

public interface ColumnFactory {
    void mappingColumn(String pattern, String tableName);
    List<String> getColumnNameList(String tableName);
    List<Object> getConverterList(String tableName);
}
