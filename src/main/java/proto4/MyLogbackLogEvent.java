//package proto4;
//
//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.Logger;
//import ch.qos.logback.classic.spi.LoggingEvent;
//import org.slf4j.Marker;
//
//
//public class MyLogbackLogEvent extends LoggingEvent {
//    public MyLogbackLogEvent(final Logger logger,final String localFQCN, final Marker marker, final Level level, final String msg, final Object[] params,
//                             final Throwable t) {
//        super(localFQCN,logger,level,msg,t,params);
//        setPrePostMessage();
//    }
//
////    public MyLogbackLogEvent(final String loggerName, final Marker marker, final String fqcn, final StackTraceElement location, final Level level, final Message data,
////                             final List<Property> properties, final Throwable t) {
////        super(loggerName, marker, fqcn, location, level, data, properties, t);
////        setPrePostMessage();
////    }
//
//    private void setPrePostMessage() {
//
//    }
//}
