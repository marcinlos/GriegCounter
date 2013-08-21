package pl.edu.agh.ki.grieg.processing.core.config.evaluator;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

/**
 * Thrown when an error during property processing occurs. Contains precise
 * information about the failure and the property that caused it.
 * 
 * @author los
 */
public class PropertyException extends ConfigException {

    /** Name of the property that caused the error */
    private final String propertyName;

    /**
     * Creates {@link PropertyException} with information only about the
     * property that caused it.
     * 
     * @param propertyName
     *            Name of the offending property
     */
    public PropertyException(String propertyName) {
        super(formatMessage(propertyName));
        this.propertyName = propertyName;
    }

    /**
     * Creates {@link PropertyException} with information about the property
     * that caused it and the exception that was thrown.
     * 
     * @param propertyName
     *            Name of the property
     * @param cause
     *            Exception describing the underlying problem
     */
    public PropertyException(String propertyName, Throwable cause) {
        super(formatMessage(propertyName), cause);
        this.propertyName = propertyName;
    }

    /**
     * @return Name of the property that caused the error
     */
    public String getPropertyName() {
        return propertyName;
    }

    private static final String formatMessage(String propertyName) {
        return "Error while processing property [" + propertyName + "]";
    }

}
