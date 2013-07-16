package pl.edu.agh.ki.grieg.analysis;

import pl.edu.agh.ki.grieg.utils.iteratee.AbstractEnumerator;
import pl.edu.agh.ki.grieg.utils.iteratee.Enumeratee;
import pl.edu.agh.ki.grieg.utils.iteratee.State;

public class WindowApplier extends AbstractEnumerator<float[][]> implements
        Enumeratee<float[][], float[][]> {

    private final float[] window;

    public WindowApplier(int size) {
        window = createHammingWindow(size);
    }

    private float[] createHammingWindow(int bufferSize) {
        float[] b = new float[bufferSize];
        int lower = bufferSize / 4;
        int upper = bufferSize - lower;
        for (int i = 0; i < bufferSize; i++) {
            if (i < lower || i > upper) {
                float x = (float) i / (float) (bufferSize - 1);
                double arg = Math.PI + Math.PI * 4.0f * x;
                b[i] = 0.5f * (1.0f + (float) Math.cos(arg));
            } else {
                b[i] = 1.0f;
            }
        }
        return b;
    }

    @Override
    public State step(float[][] item) {
        int size = item[0].length;
        float[][] copy = new float[item.length][size];
        for (int i = 0; i < item.length; ++ i) {
            System.arraycopy(item[i], 0, copy[i], 0, size);
            for (int j = 0; j < size; ++ j) {
                item[i][j] *= window[j];
            }
        }
        return null;
    }

    @Override
    public void finished() {
        endOfStream();
    }

    @Override
    public void failed(Throwable e) {
        failure(e);
    }

}
