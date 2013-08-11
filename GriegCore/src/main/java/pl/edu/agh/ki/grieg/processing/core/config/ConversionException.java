package pl.edu.agh.ki.grieg.processing.core.config;

/**
 * Thrown when the configuration requires string value conversion that cannot be
 * performed, either because of lack of appropriate parser or due to malformed
 * intput string.
 * 
 * @author los
 */
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
