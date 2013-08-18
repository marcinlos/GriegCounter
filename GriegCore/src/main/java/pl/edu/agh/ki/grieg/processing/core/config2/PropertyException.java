package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

public class PropertyException extends ConfigException {
    
    private final String propertyName;

    public PropertyException(String propertyName) {
        super(formatMessage(propertyName));
        this.propertyName = propertyName;
    }

    public PropertyException(String propertyName, Throwable cause) {
        super(formatMessage(propertyName), cause);
        this.propertyName = propertyName;
    }
    
    public String getPropertyName() {
        return propertyName;
    }
    
    private static final String formatMessage(String propertyName) {
        return "Error while processing property [" + propertyName + "]";
    }

}
