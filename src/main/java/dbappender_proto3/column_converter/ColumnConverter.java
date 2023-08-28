package dbappender_proto3.column_converter;

public interface ColumnConverter {
    String getDateColumnName();
    String getLevelColumnName();
    String getThreadColumnName();
    String getLoggerColumnName();
    String getMessageColumnName();
    String getClassColumnName();
    String getMethodColumnName();
    String getLineColumnName();
    String getFileColumnName();
    String getExceptionColumnName();
    String getContextNameColumnName();
    String getCallerColumnName();
    String getGuidColumnName();
    String getLogDebugColumnName();
    String getLogIdName();
}
