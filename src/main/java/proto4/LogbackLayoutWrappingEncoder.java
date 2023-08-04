package proto4;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.EncoderBase;
import ch.qos.logback.core.spi.ContextAware;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class LogbackLayoutWrappingEncoder<E> extends EncoderBase<E> {
    protected Layout<E> layout;

    /**
     * The charset to use when converting a String into bytes.
     * <p/>
     * By default this property has the value
     * <code>null</null> which corresponds to
     * the system's default charset.
     */
    private Charset charset;

    ContextAware parent;
    Boolean immediateFlush = null;

    public Layout<E> getLayout() {
        return layout;
    }

    public void setLayout(Layout<E> layout) {
        this.layout = layout;
    }

    public Charset getCharset() {
        return charset;
    }

    /**
     * Set the charset to use when converting the string returned by the layout
     * into bytes.
     * <p/>
     * By default this property has the value
     * <code>null</null> which corresponds to
     * the system's default charset.
     *
     * @param charset
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * Sets the immediateFlush option. The default value for immediateFlush is 'true'. If set to true,
     * the doEncode() method will immediately flush the underlying OutputStream. Although immediate flushing
     * is safer, it also significantly degrades logging throughput.
     *
     * @since 1.0.3
     */
    public void setImmediateFlush(boolean immediateFlush) {
        addWarn("As of version 1.2.0 \"immediateFlush\" property should be set within the enclosing Appender.");
        addWarn("Please move \"immediateFlush\" property into the enclosing appender.");
        this.immediateFlush = immediateFlush;
    }

    @Override
    public byte[] headerBytes() {
        if (layout == null)
            return null;

        StringBuilder sb = new StringBuilder();
        appendIfNotNull(sb, layout.getFileHeader());
        appendIfNotNull(sb, layout.getPresentationHeader());
        if (sb.length() > 0) {
            // If at least one of file header or presentation header were not
            // null, then append a line separator.
            // This should be useful in most cases and should not hurt.
            sb.append(CoreConstants.LINE_SEPARATOR);
        }
        return convertToBytes(sb.toString());
    }

    @Override
    public byte[] footerBytes() {
        if (layout == null)
            return null;

        StringBuilder sb = new StringBuilder();
        appendIfNotNull(sb, layout.getPresentationFooter());
        appendIfNotNull(sb, layout.getFileFooter());
        return convertToBytes(sb.toString());
    }

    private byte[] convertToBytes(String s) {
        if (charset == null) {
            return s.getBytes();
        } else {
            return s.getBytes(charset);
        }
    }

    public byte[] encode(E event) {
        LoggingEvent le = (LoggingEvent) event;
        String msg=le.getFormattedMessage();
//        msg="[GUID-123325234532412493] " + msg + " (proto4.Main.main:15)";

        Logger logger = (Logger) LoggerFactory.getLogger(le.getLoggerName());
        String FQCN = ch.qos.logback.classic.Logger.class.getName();

        LogbackLoggingEvent newLe = new LogbackLoggingEvent(FQCN,logger,le.getLevel(),msg,null,le.getArgumentArray());

        String txt = layout.doLayout((E) newLe);

        return convertToBytes(txt);
    }

    public boolean isStarted() {
        return false;
    }

    public void start() {
        if (immediateFlush != null) {
            if (parent instanceof OutputStreamAppender) {
                addWarn("Setting the \"immediateFlush\" property of the enclosing appender to " + immediateFlush);
                @SuppressWarnings("unchecked")
                OutputStreamAppender<E> parentOutputStreamAppender = (OutputStreamAppender<E>) parent;
                parentOutputStreamAppender.setImmediateFlush(immediateFlush);
            } else {
                addError("Could not set the \"immediateFlush\" property of the enclosing appender.");
            }
        }
        started = true;
    }

    public void stop() {
        started = false;
    }

    private void appendIfNotNull(StringBuilder sb, String s) {
        if (s != null) {
            sb.append(s);
        }
    }

    /**
     * This method allows RollingPolicy implementations to be aware of their
     * containing appender.
     *
     * @param parent
     */
    public void setParent(ContextAware parent) {
        this.parent = parent;
    }
}
