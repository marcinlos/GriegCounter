package pl.edu.agh.ki.grieg.decoder.util;

public class PCM {
    
    private static final float MAX_BYTE = Byte.MAX_VALUE;
    private static final float MAX_SHORT = Short.MAX_VALUE;

    private PCM() {
        // empty
    }
    
    public static float fromByte(byte b) {
        return b / MAX_BYTE;
    }
    
    public static float fromShort(short s) {
        return s / MAX_SHORT;
    }
    
    public static final short toShort(float a) {
        return (short) (a * Short.MAX_VALUE);
    }

}
