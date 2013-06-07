package pl.edu.agh.ki.grieg.data;

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

    public SourceDetails() {
        this.name = "?";
        this.length = UNKNOWN;
        this.sampleCount = UNKNOWN;
        this.format = null;
    }

    public SourceDetails(String name, float length, long sampleCount,
            Format format) {
        this.name = name;
        this.length = length;
        this.sampleCount = sampleCount;
        this.format = format;
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

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

}
