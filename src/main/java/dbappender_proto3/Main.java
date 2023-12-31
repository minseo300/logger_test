package dbappender_proto3;

import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;

public class Main {
    public static void main(String[] args) {
        AppenderManager am = AppenderManager.getInstance();
        Info info = Info.getInstance();
        am.createAppender();

        if(info.framework.equals("logback")) {
            am.logbackFactory.createLogger((LogbackDBAppender) am.getAppenderRegistry().get(info.appenderName));
            for (int i=0;i<50;i++) {
                am.logbackFactory.logger.info("logback: {}",i);
            }
        } else {
            am.log4j2Factory.createLogger((JdbcAppender) am.getAppenderRegistry().get(info.appenderName));
            for (int i=0;i<50;i++) {
                am.log4j2Factory.logger.info("log4j2: {}",i);
//                try {
//                    int n1 = 12, n2 = 0;
//                    int ret = n1 / n2;
//                } catch (Exception e) {
//                    System.out.println(e.getMessage().getBytes().length);
//                    System.out.println(e);
//                    am.log4j2Factory.logger.error("exception :{}",e.getMessage(),e);
//                }
            }
        }
    }
}
