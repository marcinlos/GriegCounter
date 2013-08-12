package pl.edu.agh.ki.grieg.processing.util.xml;

/**
 * Thrown when a parsing error is encountered during processing XML document.
 * 
 * @author los
 */
public class XmlParseException extends XmlException {

    public XmlParseException() {
        // empty
    }

    public XmlParseException(String message) {
        super(message);
    }

    public XmlParseException(Throwable cause) {
        super(cause);
    }

    public XmlParseException(String message, Throwable cause) {
        super(message, cause);
    }

}