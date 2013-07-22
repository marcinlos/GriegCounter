package pl.edu.agh.ki.grieg.utils;

import static com.google.common.base.Preconditions.checkArgument;

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
        checkArgument(n >= 0, "Negative step size");
        while (index + n >= period) {
            int difference = period - index;
            n -= difference;
            total += difference;
            index = 0;
            execute();
        }
        index += n;
        total += n;
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
