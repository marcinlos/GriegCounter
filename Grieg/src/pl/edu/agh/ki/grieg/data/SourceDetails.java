package pl.edu.agh.ki.grieg.data;

import pl.edu.agh.ki.grieg.meta.TagSet;

/**
 * 
 * 
 * @author los
 */
public class SourceDetails {

    public final static int UNKNOWN = -1;

    private String name;
    private float length;
    private long sampleCount;
    private Format2 format;
    private TagSet tags;

    public SourceDetails() {
        this.name = "?";
        this.length = UNKNOWN;
        this.sampleCount = UNKNOWN;
        this.format = null;
    }

    public SourceDetails(String name, float length, long sampleCount,
            Format2 format, TagSet tags) {
        this.name = name;
        this.length = length;
        this.sampleCount = sampleCount;
        this.format = format;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Format2 getFormat() {
        return format;
    }

    public void setFormat(Format2 format) {
        this.format = format;
    }

    public TagSet getTags() {
        return tags;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String len = String.format("%.1fs", length);
        sb.append(name == null ? "(-)" : name).append(" ").append(len)
                .append(" (").append(sampleCount).append(") fmt: ")
                .append(format);

        return sb.toString();
    }

}
