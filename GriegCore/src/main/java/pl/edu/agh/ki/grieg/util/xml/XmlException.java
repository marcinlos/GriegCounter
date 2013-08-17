package pl.edu.agh.ki.grieg.util.xml;

/**
 * Thrown when some error occurs during XML processing. 
 * 
 * @author los
 */
public class XmlException extends Exception {

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
