package pl.edu.agh.ki.grieg.processing.core.config.evaluator;

/**
 * Wrapper for exceptions thrown by content handlers during content evaluation.
 * 
 * @author los
 */
public class ContentHandlerException extends ValueException {

    /** Content that caused the exception */
    private final Object content;

    /**
     * Creates new {@link ContentHandlerException} with information about the
     * content that caused the exception and the exception thrown by the content
     * handler.
     * 
     * @param content
     *            Content that caused the exception
     * @param cause
     *            Exception thrown by the content handler
     */
    public ContentHandlerException(Object content, Throwable cause) {
        super(formatMessage(content), cause);
        this.content = content;
    }

    /**
     * @return Content that caused the exception
     */
    public Object getContent() {
        return content;
    }

    private static String formatMessage(Object content) {
        String typeName = content == null ? "-" : content.getClass().getName();
        String fmt = "Content handler invoked with \"%s\" [%s] failed";
        return String.format(fmt, content, typeName);
    }

}
