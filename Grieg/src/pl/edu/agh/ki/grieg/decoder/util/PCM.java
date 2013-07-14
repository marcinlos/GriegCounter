package pl.edu.agh.ki.grieg.decoder.util;

/**
 * Auxilary class for dealing with various encodings of PCM data.
 * 
 * @author los
 */
public class PCM {

    private static final float MAX_BYTE = Byte.MAX_VALUE;
    private static final float MAX_SHORT = Short.MAX_VALUE;

    private PCM() {
        // non-instantiable
    }

    /**
     * Converts a 8-bit unsigned sample to float.
     * 
     * @param b
     *            Unsigned byte representing a sample
     * @return Float representing the same sample
     */
    public static float fromUnsignedByte(int b) {
        return (b + Byte.MIN_VALUE) / MAX_BYTE;
    }

    /**
     * Converts a 16-bit signed sample to float.
     * 
     * @param s
     *            Signed short representing a sample
     * @return Float representing the same sample
     */
    public static float fromSignedShort(int s) {
        return s / MAX_SHORT;
    }

    /**
     * Converts a float sample to 16-bit signed one.
     * 
     * @param a
     *            Float representing a sample
     * @return 16-bit signed integer representing the same sample
     */
    public static final short toSignedShort(float a) {
        return (short) (a * Short.MAX_VALUE);
    }

}
