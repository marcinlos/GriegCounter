package pl.edu.agh.ki.grieg.analysis;

import pl.edu.agh.ki.grieg.util.iteratee.Function;
import pl.edu.agh.ki.grieg.util.iteratee.SimpleEnumeratee;

public class PowerSpectrum extends SimpleEnumeratee<float[][], float[]> {

    public PowerSpectrum() {
        super(NORM);
    }

    private static final Function<float[][], float[]> NORM = new Function<float[][], float[]>() {

        @Override
        public float[] apply(float[][] x) {
            int n = x[0].length;
            float[] out = new float[n];

            for (int i = 0; i < n; ++i) {
                float power = 0;
                for (float[] part : x) {
                    power += part[i] * part[i];
                }
                out[i] = power;
            }
            return out;
        }
    };

}
