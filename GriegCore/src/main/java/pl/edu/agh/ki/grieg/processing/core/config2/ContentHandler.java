package pl.edu.agh.ki.grieg.processing.core.config2;

/**
 * Interface of complex content converter. Each value node qualifier (e.g.
 * namespace URI in case of XML configuration) is associated with content
 * handler that is used to evaluate it.
 * 
 * @author los
 * 
 * @param <T>
 *            Type of the supported content
 */
public interface ContentHandler<T> {

    /**
     * Evaluates the specified content.
     * 
     * @param content
     *            Content to evaluate
     * @return Value of the content
     * @throws ValueException
     *             If the content cannot be evaluated
     */
    Object evaluate(T content) throws ValueException;

}
