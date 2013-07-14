package pl.edu.agh.ki.grieg.data;

import java.util.Arrays;

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
        Object[] vals = { depth, encoding, endianess, signed };
        return Arrays.hashCode(vals);
    }

}
