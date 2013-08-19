package pl.edu.agh.ki.grieg.processing.core.config2;

public class ContentHandlerException extends ValueException {
    
    private final Object content;
    
    public ContentHandlerException(Object content, Throwable cause) {
        super(formatMessage(content), cause);
        this.content = content;
    }
    
    public Object getContent() {
        return content;
    }
    
    private static String formatMessage(Object content) {
        String typeName = content == null ? "-" : content.getClass().getName();
        String fmt = "Content handler invoked with \"%s\" [%s] failed";
        return String.format(fmt, content, typeName);
    }

}
