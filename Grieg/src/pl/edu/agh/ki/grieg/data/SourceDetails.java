package pl.edu.agh.ki.grieg.data;

import pl.edu.agh.ki.grieg.meta.TagSet;

/**
 * 
 * 
 * @author los
 */
public class SourceDetails {

    public final static int UNKNOWN = -1;

    private float length;
    private long sampleCount;
    private SoundFormat format;
    private TagSet tags;

    public SourceDetails() {
        this.length = UNKNOWN;
        this.sampleCount = UNKNOWN;
        this.format = null;
    }

    public SourceDetails(float length, long sampleCount, SoundFormat format,
            TagSet tags) {
        this.length = length;
        this.sampleCount = sampleCount;
        this.format = format;
        this.tags = tags;
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

    public TagSet getTags() {
        return tags;
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
