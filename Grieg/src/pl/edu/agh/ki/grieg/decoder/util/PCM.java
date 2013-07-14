package pl.edu.agh.ki.grieg.decoder.util;

public class PCM {
    
    private static final float MAX_BYTE = Byte.MAX_VALUE;
    private static final float MAX_SHORT = Short.MAX_VALUE;

    private PCM() {
        // empty
    }
    
    public static float fromUnsignedByte(int b) {
        return (b + Byte.MIN_VALUE) / MAX_BYTE;
    }
    
    public static float fromSignedShort(int s) {
        return s / MAX_SHORT;
    }
    
    public static final short toSignedShort(float a) {
        return (short) (a * Short.MAX_VALUE);
    }

}
