package pl.edu.agh.ki.grieg.decoder.wav;

import pl.edu.agh.ki.grieg.data.SoundFormat;


/**
 * 
 * 
 * @author los
 */
public class AudioDetails {

    public final static int UNKNOWN = -1;

    private float length;
    private long sampleCount;
    private SoundFormat format;

    public AudioDetails() {
        this.length = UNKNOWN;
        this.sampleCount = UNKNOWN;
        this.format = null;
    }

    public AudioDetails(float length, long sampleCount, SoundFormat format) {
        this.length = length;
        this.sampleCount = sampleCount;
        this.format = format;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public long getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(long sampleCount) {
        this.sampleCount = sampleCount;
    }

    public SoundFormat getFormat() {
        return format;
    }

    public void setFormat(SoundFormat format) {
        this.format = format;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String len = String.format("%.1fs", length);
        sb.append("snd ").append(len).append(" (").append(sampleCount)
                .append(") fmt: ").append(format);
        return sb.toString();
    }

}
