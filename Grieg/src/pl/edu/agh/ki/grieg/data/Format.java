package pl.edu.agh.ki.grieg.data;

/**
 * Audio format structure, consisting of basic PCM parameters.
 * 
 * @author los
 */
public class Format {

    /** Number of audio channels */
    public final int channels;

    /** Sampling frequency */
    public final int sampleRate;

    public Format(int channels, int sampleRate) {
        this.channels = channels;
        this.sampleRate = sampleRate;
    }

    @Override
    public String toString() {
        return String.format("%d ch, %.1fkHz", channels, sampleRate / 1000.0f);
    }

}
