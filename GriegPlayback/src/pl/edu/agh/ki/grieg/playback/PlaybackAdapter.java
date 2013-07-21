package pl.edu.agh.ki.grieg.playback;

/**
 * Empty implementation of {@link PlaybackListener}, for convenience of users
 * who do not wish to explicitly receive notifications about some of the
 * playback events.
 * 
 * @author los
 */
public class PlaybackAdapter implements PlaybackListener {

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
    public void paused() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resumed() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopped() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moved(Timestamp time) {
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
