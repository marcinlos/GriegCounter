package pl.edu.agh.ki.grieg.utils;


public final class Bytes {

    private Bytes() {
        // empty
    }
    
    public static byte cut(byte b) {
        return b;
    }
    
    public static byte cut(short n) {
        return (byte) (n & 0xff);
    }
    
    public static byte cut(int n) {
        return (byte) (n & 0xff);
    }
    
    public static int cut(byte b, int weight) {
        return b << (weight * 8);
    }
    
    public static int cut(short n, int weight) {
        return (n & 0xff) << (weight * 8);
    }
    
    public static int cut(int n, int weight) {
        return (n & 0xff) << (weight * 8);
    }
    
    public static int toBE(short n) {
        return ((n & 0xff) << 1) | ((n >> 8) & 0xff);
    }
    
    public static int toBE(int n) {
        return cut(n, 3) | cut(n >> 8, 2) | cut (n >> 16, 1) | cut(n >> 24);
    }
    
}
