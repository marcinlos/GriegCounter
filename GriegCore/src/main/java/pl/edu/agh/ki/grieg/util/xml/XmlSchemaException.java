package pl.edu.agh.ki.grieg.util.xml;

/**
 * Thrown when an error caused by the schema issues is encountered during XML
 * document processing.
 * 
 * @author los
 */
public class XmlSchemaException extends XmlException {

    public XmlSchemaException(String message) {
        super(message);
    }

    public XmlSchemaException(Throwable cause) {
        super(cause);
    }

    public XmlSchemaException(String message, Throwable cause) {
        super(message, cause);
    }

}
