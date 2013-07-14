package pl.edu.agh.ki.grieg.utils;

import com.google.common.base.Charsets;
import com.google.common.primitives.Ints;

/**
 * Utility class containing methods for bit manipulations.
 * 
 * @author los
 */
public final class Bytes {

    public static int asciiToIntBE(String text) {
        byte[] array = text.getBytes(Charsets.UTF_8);
        if (array.length != 4) {
            throw new IllegalArgumentException("Text should be exactly 4 bytes");
        }
        return Ints.fromByteArray(array);
    }
    
    public static String intToStringBE(int n) {
        byte[] array = Ints.toByteArray(n);
        return new String(array, Charsets.UTF_8);
    }
}
