package pl.edu.agh.ki.grieg.processing.core.config2;

/**
 * Provides a mapping of configuration qualifiers to associated
 * {@link ContentHandler}s, used to evaluate complex content.
 * 
 * @author los
 */
public interface ContentHandlerProvider {

    /**
     * Returns {@link ContentHandler} for specified qualifier, or {@code null}
     * if the qualifier is not bound to any handler.
     * 
     * @param qualifier
     *            Configuration element qualifier
     * @return Handler for the specified qualifier, or {@code null}
     */
    ContentHandler<?> forQualifier(String qualifier);

}
