//package proto4;
//
//import ch.qos.logback.classic.PatternLayout;
//import ch.qos.logback.classic.spi.ILoggingEvent;
//
//public class LogbackPatternLayoutEncoder extends LogbackPatternLayoutEncoderBase<ILoggingEvent> {
//    @Override
//    public void start() {
//        PatternLayout patternLayout = new PatternLayout();
//        patternLayout.setContext(context);
//        patternLayout.setPattern(getPattern());
//        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
//        patternLayout.start();
//        this.layout = patternLayout;
//        super.start();
//    }
//}
