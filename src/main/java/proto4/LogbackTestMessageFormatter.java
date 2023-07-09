package proto4;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

public class LogbackTestMessageFormatter extends LayoutBase<ILoggingEvent> {

    @Override
    public String doLayout(ILoggingEvent event) {

        StringBuilder guidBuilder = new StringBuilder();

//        if (event instanceof ProObjectLogEvent) {
//            ProObjectLogEvent poLogEvent = (ProObjectLogEvent) event;
//            GUID guid = poLogEvent.getGuid();
//
//            if (guid != null) {
//                guidBuilder.append("[GUID-").append(guid).append("]");
//            }
//        }
//        toAppendTo.append(guidBuilder.toString());


        StringBuffer sbuf = new StringBuffer(128);
        sbuf.append(" ");
        sbuf.append(event.getLevel());
        sbuf.append(" [");
        sbuf.append(event.getThreadName());
        sbuf.append("] ");
        sbuf.append("guid djfadsklfjsdlkfjaslfkjds ");
        sbuf.append(event.getLoggerName());
        sbuf.append(" - ");
        sbuf.append(event.getFormattedMessage()+"\n");
//        sbuf.append(CoreConstants.LINE_SEP);
        return sbuf.toString();
    }
}
