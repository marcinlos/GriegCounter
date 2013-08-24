package pl.edu.agh.ki.grieg.util;

/**
 * Empty implementation of {@link ProgressListener}, so that the user can define
 * only the callback she needs.
 * 
 * @author los
 */
public class ProgressAdapter implements ProgressListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void started() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void progress(float percent) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finished() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failed(Exception e) {
        // empty
    }

}
