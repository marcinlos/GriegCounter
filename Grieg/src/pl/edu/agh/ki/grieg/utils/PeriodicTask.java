package pl.edu.agh.ki.grieg.utils;

import com.google.common.base.Preconditions;

/**
 * Simple utility class abstracting periodical execution of some task.
 * 
 * @author los
 */
public abstract class PeriodicTask {

    /** Interval between consecutive executions */
    private final int period;

    /** Number of steps since previous execution */
    private int index;

    /** Total number of steps so far */
    private int total;

    public PeriodicTask(int period, int initIndex) {
        this.period = period;
        this.index = initIndex;
    }

    public PeriodicTask(int period) {
        this(period, 0);
    }

    /**
     * Task, i.e. method executed every time the required number of steps has
     * been made.
     */
    protected abstract void execute();

    /**
     * Makes a single step, returns new value of index. Equivalent to
     * {@code step(1)}.
     * 
     * @return New value of index
     */
    public int step() {
        return step(1);
    }

    /**
     * Makes arbitrarily large step
     * 
     * @param n
     *            Number of single steps to make
     * @return New value of the index
     */
    public int step(int n) {
        Preconditions.checkArgument(n >= 0, "Negative step size");
        index += n;
        total += n;
        while (index >= period) {
            index -= period;
            execute();
        }
        return index;
    }

    /**
     * @return Number of steps since previous execution
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return Total number of steps so far
     */
    public int getTotal() {
        return total;
    }

}
