package pl.edu.agh.ki.grieg.data;

/**
 * Audio format structure, consisting of basic PCM parameters.
 * 
 * @author los
 */
public class Format {
    
    public final int channels;
    public final int bitDepth;
    public final float sampleRate;

    public Format(int channels, int bitDepth, float sampleRate) {
        this.channels = channels;
        this.bitDepth = bitDepth;
        this.sampleRate = sampleRate;
    }

}
