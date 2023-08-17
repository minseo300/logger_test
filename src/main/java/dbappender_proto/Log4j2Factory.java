package dbappender_proto;

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
import org.slf4j.Marker;

import java.util.ArrayList;

public class Log4j2Factory{
    public Logger logger;
    private LoggerContext context = (LoggerContext) LogManager.getContext(false);
    private Configuration configuration = context.getConfiguration();
    public void createLogger(LoggerConfigInfo info) {
        JdbcAppender appender = configDBAppender(info);
        AppenderRef ref = AppenderRef.createAppenderRef(info.getAppenderName(), null,null);
        AppenderRef[] refs = new AppenderRef[]{ref};
        LoggerConfig loggerConfig = null;

        loggerConfig = LoggerConfig.createLogger(false, Level.valueOf("info"),"dbTester","true",refs,null,configuration,null);
        loggerConfig.addAppender(appender, null, null);
        configuration.addLogger("dbTester",loggerConfig);
        context.updateLoggers();
        configuration.start();

        this.logger = (Logger) LogManager.getLogger("dbTester");
    }
    public void info(String msg) {
        logger.info(msg);
    }
    private JdbcAppender configDBAppender(LoggerConfigInfo info) {
        JdbcAppender.Builder builder = JdbcAppender.newBuilder();
        ArrayList<ColumnConfig> columnConfigs = new ArrayList<>();
        int i=0;
        if(info.getLogDate()) {
            ColumnConfig.Builder columnBuilder = ColumnConfig.newBuilder();
            columnBuilder.setName("LOG_DATE").setEventTimestamp(true).build();
            columnConfigs.add(i++,columnBuilder.build());
        }
        if(info.getException()) {
            ColumnConfig.Builder columnBuilder = ColumnConfig.newBuilder();
            columnBuilder.setName("EXCEPTION").setPattern("%ex{full}").build();
            columnConfigs.add(i++,columnBuilder.build());
        }
        if(info.getMessage()) {
            ColumnConfig.Builder columnBuilder = ColumnConfig.newBuilder();
            columnBuilder.setName("MESSAGE").setPattern("%message").build();
            columnConfigs.add(i++,columnBuilder.build());
        }
        if(info.getLoggerName()) {
            ColumnConfig.Builder columnBuilder = ColumnConfig.newBuilder();
            columnBuilder.setName("LOGGER_NAME").setPattern("%logger").build();
            columnConfigs.add(i++,columnBuilder.build());
        }
        if(info.getLogLevel()) {
            ColumnConfig.Builder columnBuilder = ColumnConfig.newBuilder();
            columnBuilder.setName("LEVEL").setPattern("%level").build();
            columnConfigs.add(i++,columnBuilder.build());
        }
        ConnectionSource connectionSource = FactoryMethodConnectionSource.createConnectionSource("dbappender_proto.LogConnectionPool","getConnection");

        builder.setName(info.getAppenderName());
        builder.setColumnConfigs(columnConfigs.toArray(new ColumnConfig[columnConfigs.size()]))
                .setTableName(info.getTableName())
                .setConnectionSource(connectionSource)
                .build();
        JdbcAppender dbAppender = builder.build();
        dbAppender.start();
        configuration.addAppender(dbAppender);

        return dbAppender;
    }


}
