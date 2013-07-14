package pl.edu.agh.ki.grieg.data;

import java.util.Arrays;

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

    public int getSampleRate() {
        return sampleRate;
    }

    public int getChannels() {
        return channels;
    }

    @Override
    public String toString() {
        String rate = String.format("%.1fkHz", sampleRate / 1000.0f);
        return String.format("%d x %.1fkHz", channels, rate);
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
        return Arrays.hashCode(new Object[]{sampleRate, channels});
    }

}
