package pl.edu.agh.ki.grieg.playback;

/**
 * Interface for receiving notifications about playback events.
 * 
 * @author los
 */
public interface PlaybackListener {

    /**
     * Invoked when the playback starts
     */
    void started();

    /**
     * Invoked when the playback is paused
     */
    void paused();

    /**
     * Invoked when the playback is resumed after pause
     */
    void resumed();

    /**
     * Invoked when the playback is definitely stopped
     */
    void stopped();

    /**
     * Invoked when an error occures
     * 
     * @param e
     *            Exception being the symptom of an error
     */
    void failed(Exception e);

}
