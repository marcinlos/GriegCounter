package pl.edu.agh.ki.grieg.data;

/**
 * Parameters of decoded, converted to float PCM data.
 * 
 * @author los
 */
public class Format2 {

    /** Number of audio channels */
    public final int channels;

    /** Sampling frequency */
    public final int sampleRate;

    public Format2(int channels, int sampleRate) {
        this.channels = channels;
        this.sampleRate = sampleRate;
    }

    @Override
    public String toString() {
        return String.format("%d ch, %.1fkHz", channels, sampleRate / 1000.0f);
    }

}
