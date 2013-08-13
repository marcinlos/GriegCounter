package pl.edu.agh.ki.grieg.processing.util.xml;

/**
 * Thrown when the XML document schema cannot be found.
 * 
 * @author los
 */
public class XmlSchemaNotFoundException extends XmlSchemaException {

    public XmlSchemaNotFoundException(String message) {
        super(message);
    }

    public XmlSchemaNotFoundException(Throwable cause) {
        super(cause);
    }

}
