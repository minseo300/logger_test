package proto4;


public class Loggers {
    private String loggerType;
    private boolean async;
    private String formatter;
    private String path;
    private String appender; // rolling file, console
    private String rollingPolicy;
    private String limitRollingFileNumber;
    private String deleteRollingFilePeriod;
    private boolean logDebugEnable;
    private String timeBasePolicyUnit;
    private String timeBasePolicyValue;
    private String sizeBasePolicyValue;
    private String level;

    public Loggers(String loggerType, boolean async, String formatter, String path, String appender, String rollingPolicy, String limitRollingFileNumber, String deleteRollingFilePeriod,
                   boolean logDebugEnable, String timeBasePolicyUnit, String timeBasePolicyValue, String sizeBasePolicyValue, String level) {
        this.loggerType = loggerType;
        this.async = async;
        this.formatter = formatter;
        this.path = path;
        this.appender = appender;
        this.rollingPolicy = rollingPolicy;
        this.limitRollingFileNumber = limitRollingFileNumber;
        this.deleteRollingFilePeriod = deleteRollingFilePeriod;
        this.logDebugEnable = logDebugEnable;
        this.timeBasePolicyUnit = timeBasePolicyUnit;
        this.timeBasePolicyValue = timeBasePolicyValue;
        this.sizeBasePolicyValue = sizeBasePolicyValue;
        this.level = level;
    }

    public String getLevel(){
        return this.level;
    }

    public String getSizeBasePolicyValue(){
        return this.sizeBasePolicyValue;
    }
    public String getAppender(){return this.appender;}
    public boolean getAsync(){return this.async;}

    public String getLoggerType() {
        return loggerType;
    }

    public boolean isAsync() {
        return async;
    }

    public String getFormatter() {
        return formatter;
    }

    public String getPath() {
        return path;
    }

    public String getRollingPolicy() {
        return rollingPolicy;
    }

    public String getLimitRollingFileNumber() {
        return limitRollingFileNumber;
    }

    public String getDeleteRollingFilePeriod() {
        return deleteRollingFilePeriod;
    }

    public boolean isLogDebugEnable() {
        return logDebugEnable;
    }

    public String getTimeBasePolicyUnit() {
        return timeBasePolicyUnit;
    }

    public String getTimeBasePolicyValue() {
        return timeBasePolicyValue;
    }

}
