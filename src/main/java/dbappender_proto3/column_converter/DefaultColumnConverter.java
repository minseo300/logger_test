package dbappender_proto3.column_converter;

public class DefaultColumnConverter implements ColumnConverter {
    @Override
    public String getDateColumnName() {
        return "LOG_DATE";
    }
    @Override
    public String getLevelColumnName() {
        return "LOG_LEVEL";
    }

    @Override
    public String getThreadColumnName() {
        return "THREAD_NAME";
    }

    @Override
    public String getLoggerColumnName() {
        return "LOGGER_NAME";
    }

    @Override
    public String getMessageColumnName() {
        return "MESSAGE";
    }

    @Override
    public String getClassColumnName() {
        return "CLASS";
    }

    @Override
    public String getMethodColumnName() {
        return "METHOD";
    }

    @Override
    public String getLineColumnName() {
        return "LINE";
    }

    @Override
    public String getFileColumnName() {
        return "FILE";
    }

    @Override
    public String getExceptionColumnName() {
        return "EXCEPTION";
    }

    @Override
    public String getContextNameColumnName() {
        return "CONTEXT_NAME";
    }

    @Override
    public String getCallerColumnName() {
        return "CALLER";
    }

    @Override
    public String getGuidColumnName() {
        return "GUID";
    }

    @Override
    public String getLogDebugColumnName() {
        return "LOG_DEBUG";
    }

    @Override
    public String getLogIdName() {
        return "LOG_ID";
    }
}
