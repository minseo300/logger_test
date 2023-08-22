package dbappender_proto2;

import dbappender_proto2.column_converter.LogbackColumnFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.util.ArrayList;

public class Log4j2Factory {
    private Info info = Info.getInstance();
    public Logger logger;
    private LoggerContext context = (LoggerContext) LogManager.getContext(false);
    private Configuration configuration = context.getConfiguration();
    public JdbcAppender createDBAppender() {
        JdbcAppender.Builder builder = JdbcAppender.newBuilder();
        ArrayList<ColumnConfig> columnConfigs = new ArrayList<>();
        JdbcAppender appender = builder.build();



        return appender;
    }

    public void createLogger(JdbcAppender appender) {
        AppenderRef ref = AppenderRef.createAppenderRef(info.appenderName,null,null);
        AppenderRef[] refs = new AppenderRef[]{ref};

        LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.valueOf("info"),"log4j2 DB Tester","true",refs,null,configuration, null);
    }
}
