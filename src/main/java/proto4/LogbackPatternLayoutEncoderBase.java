package proto4;

import ch.qos.logback.core.Layout;

public class LogbackPatternLayoutEncoderBase<E> extends LogbackLayoutWrappingEncoder<E>{
    String pattern;

    // due to popular demand outputPatternAsHeader is set to false by default
    protected boolean outputPatternAsHeader = false;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean isOutputPatternAsHeader() {
        return outputPatternAsHeader;
    }

    /**
     * Print the pattern string as a header in log files
     *
     * @param outputPatternAsHeader
     * @since 1.0.3
     */
    public void setOutputPatternAsHeader(boolean outputPatternAsHeader) {
        this.outputPatternAsHeader = outputPatternAsHeader;
    }

    public boolean isOutputPatternAsPresentationHeader() {
        return outputPatternAsHeader;
    }

    /**
     * @deprecated replaced by {@link #setOutputPatternAsHeader(boolean)}
     */
    public void setOutputPatternAsPresentationHeader(boolean outputPatternAsHeader) {
        addWarn("[outputPatternAsPresentationHeader] property is deprecated. Please use [outputPatternAsHeader] option instead.");
        this.outputPatternAsHeader = outputPatternAsHeader;
    }

    @Override
    public void setLayout(Layout<E> layout) {
        throw new UnsupportedOperationException("one cannot set the layout of " + this.getClass().getName());
    }

}
