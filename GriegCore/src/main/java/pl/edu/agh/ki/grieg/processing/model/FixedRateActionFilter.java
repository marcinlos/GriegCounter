package pl.edu.agh.ki.grieg.processing.model;

import java.util.concurrent.TimeUnit;

/**
 * {@link ActionFilter} implementation that allows for triggering action only
 * after the specified amount of time since last invocation has passed.
 * 
 * @author los
 */
public class FixedRateActionFilter extends ActionFilter {

    /** Time between consecutive invocations in nanoseconds */
    private final long delta;

    /**
     * Amount of nanoseconds since some arbitrary point in time when the action
     * was fired last time
     */
    private long lastFired;

    /**
     * Creates new {@link FixedRateActionFilter} with no action and the
     * specified delay between invocations.
     * 
     * @param unit
     *            Time unit
     * @param delta
     *            Amount of time
     */
    public FixedRateActionFilter(TimeUnit unit, long delta) {
        this.delta = unit.toNanos(delta);
    }

    /**
     * Creates new {@link FixedRateActionFilter} with specified action delay
     * between invocations.
     * 
     * @param unit
     *            Time unit
     * @param delta
     *            Amount of time
     */
    public FixedRateActionFilter(Runnable action, TimeUnit unit, long delta) {
        super(action);
        this.delta = unit.toNanos(delta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean shouldTrigger() {
        return System.nanoTime() - lastFired > delta;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Updates last-invocation-time.
     */
    @Override
    protected void doPerform() {
        super.doPerform();
        lastFired = System.nanoTime();
    }

}
