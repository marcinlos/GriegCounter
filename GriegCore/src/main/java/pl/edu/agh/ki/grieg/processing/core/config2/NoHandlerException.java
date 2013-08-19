package pl.edu.agh.ki.grieg.processing.core.config2;

public class NoHandlerException extends ValueException {
    
    private final String qualifier;

    public NoHandlerException(String qualifier) {
        super(formatMessage(qualifier));
        this.qualifier = qualifier;
    }
    
    public String getQualifier() {
        return qualifier;
    }
    
    private static String formatMessage(String qualifier) {
        return "No handler registered for qualifier \"" + qualifier + "\"";
    }
}
