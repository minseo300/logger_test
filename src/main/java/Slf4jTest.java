import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jTest {
    private static Logger logger= LoggerFactory.getLogger(Slf4jTest.class);

    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<500;i++){
            logger.info("hello");
            Thread.sleep(100);
        }
    }
}
