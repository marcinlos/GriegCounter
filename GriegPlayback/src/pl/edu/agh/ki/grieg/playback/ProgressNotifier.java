package pl.edu.agh.ki.grieg.playback;

import pl.edu.agh.ki.grieg.utils.PeriodicTask;
import pl.edu.agh.ki.grieg.utils.iteratee.AbstractEnumerator;
import pl.edu.agh.ki.grieg.utils.iteratee.Enumeratee;
import pl.edu.agh.ki.grieg.utils.iteratee.State;

/**
 * Class whose purpose is to translate incoming PCM data into timestamps with
 * specified density. Facilitates implementing progress monitoring.
 * 
 * @author los
 */
public class ProgressNotifier extends AbstractEnumerator<Timestamp> implements
        Enumeratee<float[][], Timestamp> {

    /** Number of samples in a second of audio data */
    private final int sampleRate;

    /** Auxilary object for tracking number of processed samples */
    private final PeriodicTask task;

    /**
     * Cretes new {@code ProgressNotifier} using specified configuration values
     * 
     * @param sampleRate
     *            Number of samples in a second of data
     * @param perSecond
     *            How often (in 1 minute) are the notifications to be
     *            broadcasted
     */
    public ProgressNotifier(int sampleRate, int perSecond) {
        this.sampleRate = sampleRate;
        int delta = sampleRate / perSecond;

        // Forward the task to private internal method
        task = new PeriodicTask(delta) {
            @Override
            protected void execute() {
                notifyListeners(getTotal());
            }
        };
    }

    /**
     * Computes the timestamp and pushes it to the listeners
     * 
     * @param sampleCount
     *            Number of samples procssed so far
     */
    private synchronized void notifyListeners(int sampleCount) {
        float seconds = sampleCount / (float) sampleRate;
        Timestamp time = new Timestamp(sampleCount, seconds / 1000);
        pushChunk(time);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State step(float[][] item) {
        final int sampleCount = item[0].length;
        // forward to PeriodicTask
        task.step(sampleCount);
        return State.Cont;
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
    public void failed(Throwable e) {
        // empty
    }

}