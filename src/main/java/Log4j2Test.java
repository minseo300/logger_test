import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.util.Map;

public class Log4j2Test {
    private static final Logger logger= (Logger) LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("HI");
        logger.info("HELLO");

    }
}
