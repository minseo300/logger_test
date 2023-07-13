package proto4;

import ch.qos.logback.core.rolling.TriggeringPolicyBase;
import ch.qos.logback.core.util.FileSize;

import java.io.File;

public class LogbackSizeBasedTriggeringPolicy extends TriggeringPolicyBase {
    public static final long DEFAULT_MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    FileSize maxFileSize = new FileSize(DEFAULT_MAX_FILE_SIZE);

    public void setMaxFileSize(FileSize aMaxFileSize) {
        this.maxFileSize = aMaxFileSize;
    }
    public LogbackSizeBasedTriggeringPolicy() {
    }

    @Override
    public boolean isTriggeringEvent(File activeFile, Object event) {
        return (activeFile.length() >= maxFileSize.getSize());
    }
}
