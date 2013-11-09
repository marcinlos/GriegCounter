package pl.edu.agh.ki.grieg.analysis;

import pl.edu.agh.ki.grieg.util.iteratee.AbstractEnumeratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;
import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

/**
 * Enumeratee computing FFT of first channel of input data. Output consists of
 * real and imaginary parts, organized as
 * 
 * <pre>
 * float out[2][size] = { real, imag }
 * </pre>
 * 
 * @author los
 */
public class FFT extends AbstractEnumeratee<float[][], float[][]> {

    /** Size of the input data */
    private int size;
    /** FFT implementation for specified size */
    private FloatFFT_1D fft;

    /*
     * Buffers - for input data (jtransforms needs 2x the size to store result
     * parts), and for output - real and imaginary part
     */
    private float[] a;
    private float[] real;
    private float[] imag;

    /** To forward two parts as 2 x size array */
    private float[][] arrays = new float[2][];

    /**
     * {@inheritDoc}
     */
    @Override
    public State step(float[][] item) {
        float[] data = item[0];

        if (data.length != size) {
            rebuild(data.length);
        }

        System.arraycopy(data, 0, a, 0, size);
        fft.complexForward(a);

        for (int i = 0; i < size; ++i) {
            real[i] = a[2 * i];
            imag[i] = a[2 * i + 1];

            // upper part of the array must be zero!
            a[2 * i] = a[2 * i + 1] = 0;
        }
        pushChunk(arrays);
        return State.Cont;
    }

    /**
     * Creates internal structures before first invocation and after input size
     * changes.
     * 
     * @param n
     *            Size of the input data that is to be handled
     */
    private void rebuild(int n) {
        a = new float[2 * n];
        fft = new FloatFFT_1D(n);
        size = n;
        real = new float[n];
        imag = new float[n];
        arrays[0] = real;
        arrays[1] = imag;
    }

}
