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
     * Invoked when the point in time being played has changed. During regular
     * playback it should be invoked relatively often, to allow frequent, smooth
     * response to changes.
     * 
     * @param time
     *            Offset in time at which the playback currently is
     */
    void moved(Timestamp time);

    /**
     * Invoked when an error occures
     * 
     * @param e
     *            Exception being the symptom of an error
     */
    void failed(Exception e);

}
