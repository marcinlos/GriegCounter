package pl.edu.agh.ki.grieg.processing.util.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Concrete implementation of SAX {@link ErrorHandler}, rethrowing all the
 * exceptions (warnings/errors/fatal errors).
 * 
 * @author los
 */
public final class StrictErrorHandler implements ErrorHandler {

    public static final ErrorHandler INSTANCE = new StrictErrorHandler();

    @Override
    public void warning(SAXParseException e) throws SAXException {
        throw e;
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        throw e;
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        throw e;
    }
}