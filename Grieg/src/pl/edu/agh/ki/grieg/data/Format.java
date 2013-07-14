package pl.edu.agh.ki.grieg.data;

import com.google.common.base.Objects;

/**
 * Immutable complete format of the sound data, consisting of both low-level
 * encoding details and higher level sound properties.
 * 
 * @author los
 */
public class Format {

    /** PCM encoding */
    private final PCMFormat pcmFormat;

    /** Sound format */
    private final SoundFormat soundFormat;

    /**
     * Creates new {@code Format} object.
     * 
     * @param pcmFormat
     *            PCM data encoding
     * @param soundFormat
     *            Sound properties
     */
    public Format(PCMFormat pcmFormat, SoundFormat soundFormat) {
        this.pcmFormat = pcmFormat;
        this.soundFormat = soundFormat;
    }

    /**
     * @return PCM encoding format
     * @see PCMFormat
     */
    public PCMFormat getPCMFormat() {
        return pcmFormat;
    }

    /**
     * @return Sound properties
     * @see SoundFormat
     */
    public SoundFormat getSoundFormat() {
        return soundFormat;
    }

    /**
     * @return Amount of bits per sample
     * @see PCMFormat#getDepth()
     */
    public int getDepth() {
        return pcmFormat.getDepth();
    }

    /**
     * @return PCM encoding
     * @see PCMFormat#getEncoding()
     */
    public PCMEncoding getEncoding() {
        return pcmFormat.getEncoding();
    }

    /**
     * @return Endianess of PCM encoding
     * @see PCMFormat#getEndianess()
     */
    public Endianess getEndianess() {
        return pcmFormat.getEndianess();
    }

    /**
     * @return {@code true} if the PCM data is signed, {@code false} otherwise
     * @see PCMFormat#isSigned()
     */
    public boolean isSigned() {
        return pcmFormat.isSigned();
    }

    /**
     * @return Amount of samples per second
     * @see SoundFormat#getSampleRate()
     */
    public int getSampleRate() {
        return soundFormat.getSampleRate();
    }

    /**
     * @return Number of channels
     * @see SoundFormat#getChannels()
     */
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
        return Objects.hashCode(pcmFormat, soundFormat);
    }

    /**
     * @return Builder object
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Auxilary class that can be used to incrementally build {@link Format}
     * object.
     */
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

        /**
         * Creates new {@linkplain Format} object, using previously set values.
         * If some value is missing, {@linkplain IllegalStateException} is
         * thrown.
         * 
         * @return New {@link Format} object
         */
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
