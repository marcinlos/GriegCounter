package pl.edu.agh.ki.grieg.data;

/**
 * Audio format structure, consisting of basic PCM parameters.
 * 
 * @author los
 */
public class Format {
    
    /** Number of audio channels */
    public final int channels;
    
    /** Number of bits per sample */
    public final int bitDepth;
    
    /** Sampling frequency */
    public final float sampleRate;
    
    public Format(int channels, int bitDepth, float sampleRate) {
        this.channels = channels;
        this.bitDepth = bitDepth;
        this.sampleRate = sampleRate;
    }

}
