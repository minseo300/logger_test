package dbappender_proto2;

public class Main {
    public static void main(String[] args) {
        AppenderManager am = AppenderManager.getInstance();
        Info info = Info.getInstance();
        am.createAppender();

        if(info.framework.equals("logback")) {
            am.logbackFactory.createLogger((LogbackDBAppender) am.getAppenderRegistry().get(info.appenderName));
            am.logbackFactory.logger.info("logback");
        } else {
            am.log4j2Factory.logger.info("log4j2");
        }
    }
}
