package pl.edu.agh.ki.grieg.processing.model;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ActionFilter {

    private static final Runnable EMPTY = new Runnable() {
        @Override
        public void run() {
            // empty
        }
    };

    protected abstract boolean shouldTrigger();

    private final Runnable action;

    protected ActionFilter() {
        this.action = EMPTY;
    }

    protected ActionFilter(Runnable action) {
        this.action = checkNotNull(action);
    }

    public final boolean perform() {
        final boolean doIt = shouldTrigger();
        if (doIt) {
            doPerform();
        }
        return doIt;
    }

    protected void doPerform() {
        action.run();
    }

    public final void forcePerform() {
        doPerform();
    }

}
