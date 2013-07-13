package pl.edu.agh.ki.grieg.utils;

/**
 * Utility class containing methods for bit manipulations.
 * 
 * @author los
 */
public final class Bytes {

    private Bytes() {
        // empty
    }

    public static int low(byte b) {
        return b & 0xff;
    }

    public static int low(short n) {
        return n & 0xff;
    }

    public static int low(int n) {
        return n & 0xff;
    }

    public static long low(long n) {
        return n & 0xff;
    }

    public static int low(byte b, int weight) {
        return b << (weight * 8);
    }

    public static int low(short n, int weight) {
        return (n & 0xff) << (weight * 8);
    }

    public static int low(int n, int weight) {
        return (n & 0xff) << (weight * 8);
    }


    public static void toBigEndian(int n, byte[] bytes) {
        bytes[0] = (byte) low(n /* , 0 */);
        bytes[1] = (byte) low(n, 1);
        bytes[2] = (byte) low(n, 2);
        bytes[3] = (byte) low(n, 3);
    }

    public static void toLittleEndian(int n, byte[] bytes) {
        bytes[0] = (byte) low(n, 3);
        bytes[1] = (byte) low(n, 2);
        bytes[2] = (byte) low(n, 1);
        bytes[3] = (byte) low(n /* , 0 */);
    }

    public static byte[] toBigEndian(int n) {
        byte[] bytes = new byte[4];
        toBigEndian(n, bytes);
        return bytes;
    }

    public static byte[] toLittleEndian(int n) {
        byte[] bytes = new byte[4];
        toLittleEndian(n, bytes);
        return bytes;
    }

    public static void toBigEndian(short n, byte[] bytes) {
        bytes[0] = (byte) low(n /* , 0 */);
        bytes[1] = (byte) low(n, 1);
    }

    public static void toLittleEndian(short n, byte[] bytes) {
        bytes[0] = (byte) low(n, 1);
        bytes[1] = (byte) low(n /* , 0 */);
    }

    public static byte[] toBigEndian(short n) {
        byte[] bytes = new byte[2];
        toBigEndian(n, bytes);
        return bytes;
    }

    public static byte[] toLittleEndian(short n) {
        byte[] bytes = new byte[2];
        toLittleEndian(n, bytes);
        return bytes;
    }

    public static int intFromBigEndian(byte[] b) {
        return low(b[0]) | low(b[1], 1) | low(b[2], 2) | low(b[3], 3);
    }

    public static int intFromLittleEndian(byte[] b) {
        return low(b[0], 3) | low(b[1], 2) | low(b[2], 1) | low(b[3]);
    }

    public static short shortFromBigEndian(byte[] b) {
        return (short) (low(b[0]) | low(b[1], 1));
    }

    public static short shortFromLittleEndian(byte[] b) {
        return (short) (low(b[0], 1) | low(b[1]));
    }

    public static int asciiToIntBE(String text) {
        byte[] array = text.getBytes(Strings.UTF_8);
        if (array.length != 4) {
            throw new IllegalArgumentException("Text should be exactly 4 bytes");
        }
        return Bytes.intFromBigEndian(array);
    }
    
    public static String intToStringBE(int n) {
        byte[] array = toBigEndian(n);
        return new String(array, Strings.UTF_8);
    }
}
