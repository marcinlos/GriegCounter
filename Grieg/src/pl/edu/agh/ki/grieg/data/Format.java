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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Integer depth;
        private PCMEncoding encoding;
        private Endianess endianess;
        private Boolean signed;
        private Integer sampleRate;
        private Integer channels;

        private void assertNotNull(Object o, String what) {
            if (o == null) {
                throw new IllegalStateException("Builder: " + what
                        + " is not set");
            }
        }

        private Builder() {
            // empty
        }

        public Builder depth(int d) {
            this.depth = d;
            return this;
        }

        public Builder encoding(PCMEncoding e) {
            this.encoding = e;
            return this;
        }

        public Builder endianess(Endianess e) {
            this.endianess = e;
            return this;
        }

        public Builder bigEndian() {
            return endianess(Endianess.BIG);
        }

        public Builder littleEndian() {
            return endianess(Endianess.LITTLE);
        }

        public Builder signed(boolean s) {
            this.signed = s;
            return this;
        }

        public Builder signed() {
            return signed(true);
        }

        public Builder unsigned() {
            return signed(false);
        }
        
        public Builder rate(int sampleRate) {
            this.sampleRate = sampleRate;
            return this;
        }
        
        public Builder channels(int ch) {
            this.channels = ch;
            return this;
        }

        public Format build() {
            assertNotNull(depth, "bit depth");
            assertNotNull(encoding, "encoding");
            assertNotNull(endianess, "endianess");
            assertNotNull(signed, "integer encoding");
            assertNotNull(sampleRate, "sample rate");
            assertNotNull(channels, "channel count");
            PCMFormat pcm = new PCMFormat(depth, encoding, endianess, signed);
            SoundFormat sound = new SoundFormat(sampleRate, channels);
            return new Format(pcm, sound);
        }

    }
}
