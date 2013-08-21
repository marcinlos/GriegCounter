package pl.edu.agh.ki.grieg.processing.core.config.evaluator;

/**
 * Error handler callback interface, used when exceptions may occur, and it is
 * undesirable to propagate them outside.
 * 
 * @author los
 */
public interface ErrorHandler {

    /**
     * Notifies the handler about an exception.
     * 
     * @param e
     *            Exception
     */
    void error(Throwable e);

}
