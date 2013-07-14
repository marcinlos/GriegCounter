package pl.edu.agh.ki.grieg.data;

import java.util.Arrays;

/**
 * Format of the sound data.
 * 
 * @author los
 */
public class Format {

    /** PCM */
    private final PCMFormat pcmFormat;

    private final SoundFormat soundFormat;

    public Format(PCMFormat pcmFormat, SoundFormat soundFormat) {
        this.pcmFormat = pcmFormat;
        this.soundFormat = soundFormat;
    }
    
    public PCMFormat getPCMFormat() {
        return pcmFormat;
    }
    
    public SoundFormat getSoundFormat() {
        return soundFormat;
    }

    public int getDepth() {
        return pcmFormat.getDepth();
    }

    public PCMEncoding getEncoding() {
        return pcmFormat.getEncoding();
    }
    
    public Endianess getEndianess() {
        return pcmFormat.getEndianess();
    }

    public boolean isSigned() {
        return pcmFormat.isSigned();
    }

    public int getSampleRate() {
        return soundFormat.getSampleRate();
    }

    public int getChannels() {
        return soundFormat.getChannels();
    }

    @Override
    public String toString() {
        return soundFormat.toString() + ", " + pcmFormat.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Format) {
            Format other = (Format) o;
            return pcmFormat.equals(other.pcmFormat)
                    && soundFormat.equals(other.soundFormat);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] { pcmFormat, soundFormat });
    }
}
