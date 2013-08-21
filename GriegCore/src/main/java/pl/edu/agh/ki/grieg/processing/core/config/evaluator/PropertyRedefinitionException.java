package pl.edu.agh.ki.grieg.processing.core.config.evaluator;

/**
 * Thrown when the property with the same name as some property previously
 * registered is encountered.
 * 
 * @author los
 */
public class PropertyRedefinitionException extends PropertyException {

    /** Name of the duplicate property */
    private final String propertyName;

    /**
     * Creates new {@link PropertyRedefinitionException} caused by the property
     * with specified name;
     * 
     * @param propertyName
     *            Name of the duplicate property
     */
    public PropertyRedefinitionException(String propertyName) {
        super(formatString(propertyName));
        this.propertyName = propertyName;
    }

    /**
     * @return Name of the duplicate property
     */
    public String getPropertyName() {
        return propertyName;
    }

    private static String formatString(String propertyName) {
        return "Property \"" + propertyName + "\" is redefined";
    }

}
