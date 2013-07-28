package pl.edu.agh.ki.grieg.analysis;

import pl.edu.agh.ki.grieg.util.iteratee.Function;
import pl.edu.agh.ki.grieg.util.iteratee.SimpleEnumeratee;

public class Power extends SimpleEnumeratee<float[][], float[]> {

    public Power() {
        super(RSM);
    }

    private static final Function<float[][], float[]> RSM = 
            new Function<float[][], float[]>() {
        @Override
        public float[] apply(float[][] x) {
            int channels = x.length;
            int samples = x[0].length;

            float[] mean = new float[channels];

            for (int i = 0; i < channels; ++i) {
                float sq = 0;
                for (float val : x[i]) {
                    sq += val * val;
                }
                mean[i] = (float) Math.sqrt(sq / samples);
            }
            return mean;
        }
    };

}
