package proto4;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

public class LogbackGuidPatternLayout extends LayoutBase<ILoggingEvent> {

    private static String prefix=null;
    private static String postfix=null;
    @Override
    public String doLayout(ILoggingEvent event) {

        StringBuilder guidBuilder = new StringBuilder();
        StringBuffer sbuf = new StringBuffer(128);


//        if (event instanceof ProObjectLogEvent) {
//            ProObjectLogEvent poLogEvent = (ProObjectLogEvent) event;
//            GUID guid = poLogEvent.getGuid();
//
//            if (guid != null) {
//                guidBuilder.append("[GUID-").append(guid).append("]");
//            }
//        }
//        toAppendTo.append(guidBuilder.toString());

        if(prefix!=null){
            sbuf.append(prefix);
        }

        sbuf.append(event.getLevel());
        sbuf.append(" [");
        sbuf.append(event.getThreadName());
        sbuf.append("] ");
        sbuf.append("guid 12384918349238409 ");
        sbuf.append(event.getLoggerName());
        sbuf.append(" - ");
        sbuf.append(event.getFormattedMessage());

        if(postfix!=null){
            sbuf.append(postfix);
        }

        sbuf.append(CoreConstants.LINE_SEPARATOR);
        return sbuf.toString();
    }
    public void setPrefix(String prefix){
        this.prefix=prefix;
    }
    public void setPostfix(String postfix){
        this.postfix=postfix;
    }
    public void setting(){

    }
}
