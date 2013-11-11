package pl.edu.agh.ki.grieg.swing.util;

import java.awt.Color;

/**
 * Static helpers for color manipulation.
 * 
 * @author los
 */
public class Colors {

    private Colors() {
        // non-instantiable
    }

    /**
     * Converts color in HSV format to {@link java.awt.Color}.
     * 
     * @param h
     *            Hue
     * @param v
     *            Value
     * @param s
     *            Saturation
     * @param a
     *            Alpha channel
     * 
     * @return {@code Color} object
     */
    public static Color hsv(float h, float s, float v, float a) {
        h %= 2 * Math.PI;
        double H = h * 3 / Math.PI;
        float c = v * s;
        float x = c * (1 - (float) Math.abs(H % 2 - 1));
        float m = v * (1 - s);

        if (H < 1) {
            return new Color(m + c, m + x, m + 0, a);
        } else if (H < 2) {
            return new Color(m + x, m + c, m + 0, a);
        } else if (H < 3) {
            return new Color(m + 0, m + c, m + x, a);
        } else if (H < 4) {
            return new Color(m + 0, m + x, m + c, a);
        } else if (H < 5) {
            return new Color(m + x, m + 0, m + c, a);
        } else {
            return new Color(m + c, m + 0, m + x, a);
        }
    }

}
