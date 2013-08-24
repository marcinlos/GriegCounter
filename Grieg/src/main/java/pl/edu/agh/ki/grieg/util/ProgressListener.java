package pl.edu.agh.ki.grieg.util;

/**
 * Generic listener for receiving notifications about progress of arbitrary
 * process.
 * 
 * @author los
 */
public interface ProgressListener {

    /**
     * Operation has just started.
     */
    void started();

    /**
     * Invoked when some progress has been made.
     * 
     * @param percent
     *            Estimation of total process advancement, in [0, 1)
     */
    void progress(float percent);

    /**
     * Invoked when the process has successfully finished.
     */
    void finished();

    /**
     * Invoked when the process has been aborted due to an exception.
     * 
     * @param e
     *            Exception that interrupted the process
     */
    void failed(Exception e);
}
