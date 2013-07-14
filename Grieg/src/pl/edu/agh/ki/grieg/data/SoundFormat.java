package pl.edu.agh.ki.grieg.data;

import com.google.common.base.Objects;

/**
 * Intrinsic, high-level sample format-agnostic properties of PCM-encoding.
 * 
 * @author los
 */
public class SoundFormat {

    /** Samples per second */
    public final int sampleRate;

    /** Number of channels */
    public final int channels;

    public SoundFormat(int sampleRate, int channels) {
        this.sampleRate = sampleRate;
        this.channels = channels;
    }

    /**
     * @return Amount of samples per second
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * @return Number of channels
     */
    public int getChannels() {
        return channels;
    }

    @Override
    public String toString() {
        String rate = String.format("%.1fkHz", sampleRate / 1000.0f);
        return String.format("%d x %s", channels, rate);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SoundFormat) {
            SoundFormat other = (SoundFormat) o;
            return sampleRate == other.sampleRate && channels == other.channels;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sampleRate, channels);
    }

}
