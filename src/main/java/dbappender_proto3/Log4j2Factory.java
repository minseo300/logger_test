package dbappender_proto3;

import dbappender_proto3.column_converter.Log4j2ColumnFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.apache.logging.log4j.core.appender.db.jdbc.FactoryMethodConnectionSource;
import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.pattern.FormattingInfo;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.logging.log4j.core.layout.PatternLayout.createPatternParser;

public class Log4j2Factory {
    private Info info = Info.getInstance();
    public Logger logger;
    private LoggerContext context = (LoggerContext) LogManager.getContext(false);
    private Configuration configuration = context.getConfiguration();
    private static Log4j2Factory log4j2Factory = new Log4j2Factory();
    public static Log4j2Factory getInstance() {
        return log4j2Factory;
    }
    public JdbcAppender createDBAppender(Log4j2ColumnFactory columnFactory) {
        JdbcAppender.Builder builder = JdbcAppender.newBuilder();
        ConnectionSource connectionSource = FactoryMethodConnectionSource.createConnectionSource("dbappender_proto3.ConnectionPool","getConnection");
        List<Object> converterList = columnFactory.getConverterList(info.tableName);
        ColumnConfig[] columnConfigs = new ColumnConfig[converterList.size()-1];
        for (int i=1 ; i< converterList.size() ; i++){
            columnConfigs[i-1] = (ColumnConfig) converterList.get(i);
        }
        builder.setName(info.appenderName);
        builder.setColumnConfigs(columnConfigs)
                .setTableName(info.tableName)
                .setConnectionSource(connectionSource)
                .build();
        JdbcAppender appender = builder.build();
        appender.start();
        configuration.addAppender(appender);

        return appender;
    }

    public void createLogger(JdbcAppender appender) {
        String loggerName = "log4j2 db tester";
        String loggerLevel = "info";

        AppenderRef ref = AppenderRef.createAppenderRef(info.appenderName,null,null);
        AppenderRef[] refs = new AppenderRef[]{ref};
        LoggerConfig loggerConfig = null;

        loggerConfig = LoggerConfig.createLogger(false, Level.valueOf(loggerLevel),loggerName,"true",refs,null,configuration,null);
        loggerConfig.addAppender(appender, null, null);
        configuration.addLogger(loggerName,loggerConfig);
        context.updateLoggers();
        configuration.start();

        this.logger = (Logger) LogManager.getLogger(loggerName);
    }
}
