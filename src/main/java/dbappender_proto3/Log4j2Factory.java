package dbappender_proto3;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.pattern.FormattingInfo;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;

import java.util.ArrayList;
import java.util.List;

import static org.apache.logging.log4j.core.layout.PatternLayout.createPatternParser;

public class Log4j2Factory {
    private Info info = Info.getInstance();
    public Logger logger;
    private LoggerContext context = (LoggerContext) LogManager.getContext(false);
    private Configuration configuration = context.getConfiguration();
    public JdbcAppender createDBAppender() {
        JdbcAppender.Builder builder = JdbcAppender.newBuilder();
        ArrayList<ColumnConfig> columnConfigs = new ArrayList<>();
        JdbcAppender appender = builder.build();
        final PatternParser parser = createPatternParser(configuration);
        final List<PatternFormatter> list = parser.parse(info.pattern);
        final PatternFormatter[] formatters = list.toArray(PatternFormatter.EMPTY_ARRAY);
        boolean hasFormattingInfo = false;
        for (PatternFormatter formatter : formatters) {
            FormattingInfo info = formatter.getFormattingInfo();
            if (info != null && info != FormattingInfo.getDefault()) {
                hasFormattingInfo = true;
                break;
            }
        }


        return appender;
    }

    public void createLogger(JdbcAppender appender) {
        AppenderRef ref = AppenderRef.createAppenderRef(info.appenderName,null,null);
        AppenderRef[] refs = new AppenderRef[]{ref};

        LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.valueOf("info"),"log4j2 DB Tester","true",refs,null,configuration, null);
    }
}
