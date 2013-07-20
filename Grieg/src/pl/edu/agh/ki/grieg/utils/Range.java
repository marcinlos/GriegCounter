package pl.edu.agh.ki.grieg.utils;

import com.google.common.base.Objects;

/**
 * Structure representing floating point interval
 * 
 * @author los
 */
public final class Range {

    /** Lower bound */
    public final float min;

    /** Upper bound */
    public final float max;

    public Range(float min, float max) {
        this.min = min;
        this.max = max;
    }

    /**
     * @return Length of the interval represented by this range
     */
    public float length() {
        return max - min;
    }

    /**
     * @return Whether the lower bounds is no greater than the upper
     */
    public boolean isValid() {
        return min <= max;
    }

    /**
     * Returns smallest possible range containing both the old range (if valid)
     * and the specified value. It may return a range upon which the method was
     * called, if it contains the value.
     * 
     * @param value
     *            Value to be contained in the returned range
     * @return Range containing the {@code value} and the original range
     */
    public Range extendWith(float value) {
        if (value < min || value > max) {
            float newMin = Math.min(min, value);
            float newMax = Math.max(max, value);
            return new Range(newMin, newMax);
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", min, max);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Range) {
            Range other = (Range) o;
            return min == other.min && max == other.max;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(min, max);
    }

}
