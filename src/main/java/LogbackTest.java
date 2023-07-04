import ch.qos.logback.classic.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LogbackTest {
    static Logger logger= (Logger) LoggerFactory.getLogger("");
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            logger.info("Rolling file appender example...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
