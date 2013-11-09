package pl.edu.agh.ki.grieg.swing.util;

public class Utils {

    private Utils() {
        // non-instantiable
    }
    
    public static float dB(float x) {
        return 10 * (float) Math.log10(x);
    }
    
    public static double clamp(double val, double min, double max) {
        return Math.max(Math.min(val, max), min);
    }
    
    public static float clamp(float val, float min, float max) {
        return Math.max(Math.min(val, max), min);
    }

}
