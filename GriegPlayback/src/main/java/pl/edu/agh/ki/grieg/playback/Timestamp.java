package pl.edu.agh.ki.grieg.playback;

import com.google.common.base.Objects;

/**
 * Value class representing point in time during the playback. Contains two
 * values: number of processed samples and milliseconds.
 * 
 * @author los
 */
public final class Timestamp {

    /** Number of samples already processed */
    public final long sample;

    /** Number of milliseconds since the beginning */
    public final float millis;

    /**
     * Creates timestamp from specified values
     */
    Timestamp(long sample, float millis) {
        this.sample = sample;
        this.millis = millis;
    }

    /**
     * Creates timestamp based on sample count and specified sample ratio
     * 
     * @param sample
     *            Number of samples already processed
     * @param sampleRate
     *            Amount of samples in a second of data
     * @return Timestamp representing this particular point int ime
     */
    static Timestamp fromSampleRate(long sample, int sampleRate) {
        float millis = sample / (float) sampleRate;
        return new Timestamp(sample, millis);
    }

    @Override
    public String toString() {
        return String.format("%.2fs [%d]", millis * 1000.0f, sample);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Timestamp) {
            Timestamp other = (Timestamp) o;
            return sample == other.sample && millis == other.millis;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sample, millis);
    }

}
