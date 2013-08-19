package pl.edu.agh.ki.grieg.processing.core.config2;

/**
 * Thrown when no {@link ContentHandler} is associated with the complex content
 * that needs evaluation.
 * 
 * @author los
 */
public class NoHandlerException extends ValueException {

    /** Qualifier with no associated handler */
    private final String qualifier;

    /**
     * Creates new {@link NoHandlerException} with information about the
     * qualifier with missing handler.
     * 
     * @param qualifier
     *            Qualifier
     */
    public NoHandlerException(String qualifier) {
        super(formatMessage(qualifier));
        this.qualifier = qualifier;
    }

    /**
     * @return Qualifier with missing handler
     */
    public String getQualifier() {
        return qualifier;
    }

    private static String formatMessage(String qualifier) {
        return "No handler registered for qualifier \"" + qualifier + "\"";
    }
}
