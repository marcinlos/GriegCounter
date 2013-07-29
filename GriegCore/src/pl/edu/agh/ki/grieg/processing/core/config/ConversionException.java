package pl.edu.agh.ki.grieg.processing.core.config;

public class ConversionException extends ConfigException {
    
    public ConversionException() {
        // empty
    }

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(Throwable cause) {
        super(cause);
    }

    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }

}
