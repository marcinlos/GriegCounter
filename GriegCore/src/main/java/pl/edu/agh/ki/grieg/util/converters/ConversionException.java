package pl.edu.agh.ki.grieg.util.converters;

/**
 * Base exception for errors occuring during attempts to convert string to an
 * object of some type.
 * 
 * @author los
 */
public class ConversionException extends Exception {

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
