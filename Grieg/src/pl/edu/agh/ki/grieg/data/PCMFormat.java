package pl.edu.agh.ki.grieg.data;

import com.google.common.base.Objects;

/**
 * Low-level details of PCM data encoding.
 * 
 * @author los
 */
public class PCMFormat {

    /** Number of bits per sample */
    public final int depth;

    /** Binary encoding of a sample */
    public final PCMEncoding encoding;

    /** Endianess of encoding */
    public final Endianess endianess;

    /** Whether the representation uses signed integer representation */
    public final boolean signed;

    public PCMFormat(int depth, PCMEncoding encoding, Endianess endianess,
            boolean signed) {
        this.depth = depth;
        this.encoding = encoding;
        this.endianess = endianess;
        this.signed = signed;
    }

    public int getDepth() {
        return depth;
    }

    public PCMEncoding getEncoding() {
        return encoding;
    }

    public Endianess getEndianess() {
        return endianess;
    }

    public boolean isSigned() {
        return signed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (encoding == PCMEncoding.FLOAT) {
            sb.append("floating-point");
        } else {
            sb.append(depth).append("-bit ");
            sb.append(signed ? "signed" : "unsigned");
        }
        sb.append(" ").append(endianess).append(" PCM");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PCMFormat) {
            PCMFormat other = (PCMFormat) o;
            return depth == other.depth && encoding == other.encoding
                    && endianess == other.endianess && signed == other.signed;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(depth, encoding, endianess, signed);
    }

    /**
     * @return Builder facilitating {@code PCMFormat} creation
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Auxilary class for building {@link PCMFormat} objects.
     */
    public static class Builder {

        private Integer depth;
        private PCMEncoding encoding;
        private Endianess endianess;
        private Boolean signed;

        private void assertNotNull(Object o, String what) {
            if (o == null) {
                throw new IllegalStateException("Builder: " + what + " is null");
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

        public PCMFormat build() {
            assertNotNull(depth, "bit depth");
            assertNotNull(encoding, "encoding");
            assertNotNull(endianess, "endianess");
            assertNotNull(signed, "integer encoding");
            return new PCMFormat(depth, encoding, endianess, signed);
        }

    }

}
