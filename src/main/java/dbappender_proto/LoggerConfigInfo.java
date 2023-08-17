package dbappender_proto;

import lombok.Getter;

@Getter
public class LoggerConfigInfo {
    public boolean logDate;
    public boolean logLevel;
    public boolean message;
    public boolean loggerName;
    public boolean logId;
    public boolean exception;
    public String appenderName;
    public String tableName;

    public LoggerConfigInfo(boolean logDate, boolean logLevel, boolean message, boolean loggerName,
                            boolean logId, boolean exception, String appenderName, String tableName){
        this.logDate = logDate;
        this.logLevel = logLevel;
        this.message = message;
        this.loggerName = loggerName;
        this.logId = logId;
        this.exception = exception;
        this.tableName = tableName;
        this.appenderName = appenderName;
    }
    public boolean getLogDate(){
        return this.logDate;
    }
    public boolean getLogLevel(){
        return this.logLevel;
    }
    public boolean getMessage(){
        return this.message;
    }
    public boolean getLoggerName(){
        return this.loggerName;
    }
    public boolean getLogId(){
        return this.logId;
    }
    public boolean getException(){
        return this.exception;
    }
    public String getAppenderName() {return this.appenderName;}

}
