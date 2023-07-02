//package proto3;
//
//import java.io.IOException;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) throws IOException, InterruptedException {
//        Scanner scanner=new Scanner(System.in);
//
//        System.out.println("Enter the logging framework (log4j2 or logback): ");
//        String loggingFrameworkType=scanner.next();
//        System.out.println("===========Start logging with "+loggingFrameworkType+"===========");
////        System.out.println("Enter the mode (default or file): ");
////        String configureMode=scanner.next();
//
//        MyLoggerManager loggerManager= MyLoggerManager.getMyLoggerManager();
//        loggerManager.init(loggingFrameworkType);
//
//        Logger1 logger1= Logger1.getLogger();
////        Logger1 logger1=loggerManager.getLogger1Logger("initial.Logger1");
//        logger1.fatal(loggerManager.getFrameworkType());
////        logger1.trace(loggerManager.getFrameworkType());
////        logger1.debug(loggerManager.getFrameworkType());
//
//        Logger2 logger2= Logger2.getLogger();
////        Logger2 logger2=loggerManager.getLogger2Logger("initial.Logger2");
//        logger2.info(loggerManager.getFrameworkType());
//        Thread.sleep(10000);
//    }
//}
