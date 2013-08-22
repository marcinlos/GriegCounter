package pl.edu.agh.ki.grieg.processing.model;

import java.util.concurrent.TimeUnit;

public class FixedRateActionFilter extends ActionFilter {

    private final long delta;

    private long lastFired;

    protected FixedRateActionFilter(TimeUnit unit, long delta) {
        this.delta = unit.toNanos(delta);
    }

    protected FixedRateActionFilter(Runnable action, TimeUnit unit, long delta) {
        super(action);
        this.delta = unit.toNanos(delta);
    }

    @Override
    protected boolean shouldTrigger() {
        return System.nanoTime() - lastFired > delta;
    }

    @Override
    protected void doPerform() {
        super.doPerform();
        lastFired = System.nanoTime();
    }

}
