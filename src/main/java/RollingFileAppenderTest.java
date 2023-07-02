import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class RollingFileAppenderTest {
    private static Logger logger= (Logger) LogManager.getLogger(RollingFileAppenderTest.class);
    public static void main(String[] args) throws InterruptedException {
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
