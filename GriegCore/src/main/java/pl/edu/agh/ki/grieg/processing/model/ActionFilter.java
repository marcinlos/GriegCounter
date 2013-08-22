package pl.edu.agh.ki.grieg.processing.model;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility class for performing certain action only when the condition specified
 * by abstract method {@link #shouldTrigger()} is fulfilled. Condition is
 * specified by subclassing, or using one of predefined subclasses.
 * 
 * @author los
 */
public abstract class ActionFilter {

    /**
     * Empty runnable, not doing anything.
     */
    private static final Runnable EMPTY = new Runnable() {
        @Override
        public void run() {
            // empty
        }
    };

    /** Wrapped action */
    private final Runnable action;

    /**
     * Creates new {@link ActionFilter} wrapping no action. This can be useful
     * if the action is complex (e.g. has parameters) so that it's difficult to
     * express as {@link Runnable}. Then, user can still benefir from the
     * condition and state tracking and use boolean value returned by
     * {@link #perform()}
     */
    protected ActionFilter() {
        this.action = EMPTY;
    }

    /**
     * Creates new {@link ActionFilter} wrapping specified action.
     * 
     * @param action
     *            Action to wrap
     */
    protected ActionFilter(Runnable action) {
        this.action = checkNotNull(action);
    }

    /**
     * Condition that must be fulfilled if the action is to be performed. It may
     * have side-effects - this class guarantees it shall be called once per
     * each {@link #perform()} invocation.
     * 
     * @return {@code true} if the action can be performed, {@code false}
     *         otherwise
     */
    protected abstract boolean shouldTrigger();

    /**
     * Performs wrapped action if the condition specified by
     * {@link #shouldTrigger()} is fulfilled.
     * 
     * @return {@code true} if the action was indeed performed, {@code false}
     *         otherwise
     */
    public final boolean perform() {
        final boolean doIt = shouldTrigger();
        if (doIt) {
            doPerform();
        }
        return doIt;
    }

    /**
     * Performs wrapped action without checking the condition specified by
     * {@link #shouldTrigger()}.
     * 
     * <p>
     * Note: {@link #shouldTrigger()} is not invoked, which may be significant
     * if it has side-effects
     */
    public final void forcePerform() {
        doPerform();
    }

    /**
     * Actually performs the action. Subclasses may override it to perform
     * additional bookkeeping, but should call superclass' {@link #doPerform()}.
     */
    protected void doPerform() {
        action.run();
    }

}
