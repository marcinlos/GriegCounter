package pl.edu.agh.ki.grieg.processing.util.xml;

/**
 * Thrown when some error occurs during XML processing. 
 * 
 * @author los
 */
public class XmlException extends Exception {

    public XmlException() {
        // empty
    }

    public XmlException(String message) {
        super(message);
    }

    public XmlException(Throwable cause) {
        super(cause);
    }

    public XmlException(String message, Throwable cause) {
        super(message, cause);
    }

}
