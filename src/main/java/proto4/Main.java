package proto4;

import java.io.IOException;
import java.util.Scanner;

/**
 * add rolling file appender basic function for logback
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner=new Scanner(System.in);

        MyLoggerManager loggerManager= MyLoggerManager.getMyLoggerManager();
        loggerManager.init();

        Logger1 logger1=Logger1.getLogger();
        for(int i=0;i<10000;i++)
        {
            logger1.info("Rolling file appender example... {}",Integer.valueOf(i));
            try{
                Thread.sleep(300);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
