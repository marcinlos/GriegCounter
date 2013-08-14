package pl.edu.agh.ki.grieg.processing.util.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Concrete implementation of SAX {@link ErrorHandler}, rethrowing all the
 * exceptions (warnings/errors/fatal errors). It is stateless, so it has been
 * made a singleton to avoid accidental redundant instantiation.
 * 
 * @author los
 */
public final class StrictErrorHandler implements ErrorHandler {
    
    /** Single, publicly available instance of {@link StrictErrorHandler} */
    public static final ErrorHandler INSTANCE = new StrictErrorHandler();

    /**
     * {@link StrictErrorHandler} is not instantiable from the outside code 
     */
    private StrictErrorHandler() {
        // non-instantiable from the outside
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>
     * This implementation unconditionally rethrows the exception.
     */
    @Override
    public void warning(SAXParseException e) throws SAXException {
        throw e;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * This implementation unconditionally rethrows the exception.
     */
    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        throw e;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * This implementation unconditionally rethrows the exception.
     */
    @Override
    public void error(SAXParseException e) throws SAXException {
        throw e;
    }
}