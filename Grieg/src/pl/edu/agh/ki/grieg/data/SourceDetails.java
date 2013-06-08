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
    private Format format;
    private TagSet tags;

    public SourceDetails() {
        this.name = "?";
        this.length = UNKNOWN;
        this.sampleCount = UNKNOWN;
        this.format = null;
    }

    public SourceDetails(String name, float length, long sampleCount,
            Format format, TagSet tags) {
        this.name = name;
        this.length = length;
        this.sampleCount = sampleCount;
        this.format = format;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public float getLength() {
        return length;
    }

    public long getSampleCount() {
        return sampleCount;
    }

    public Format getFormat() {
        return format;
    }

    public TagSet getTags() {
        return tags;
    }
    
}
