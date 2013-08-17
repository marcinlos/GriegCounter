package pl.edu.agh.ki.grieg.util.converters;

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
