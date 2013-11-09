package pl.edu.agh.ki.grieg.analysis;

import pl.edu.agh.ki.grieg.util.iteratee.AbstractEnumeratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;

public class FFT extends AbstractEnumeratee<float[][], float[][]> {

    @Override
    public State step(float[][] item) {
        float[] data = item[0];
        fft(data, data.length, true);
        float[] real = calculateReal(data, data.length);
        float[] imag = calculateImaginary(data, data.length);
        pushChunk(new float[][] { real, imag });
        return State.Cont;
    }

    /**
     * The frequency corresponding to a specific bin
     * 
     * @param samplingFrequency
     *            The Sampling Frequency of the AudioContext
     * @param blockSize
     *            The size of the block analysed
     * @param binNumber
     */
    public static float binFrequency(float samplingFrequency, int blockSize,
            float binNumber)
    {
        return binNumber * samplingFrequency / blockSize;
    }

    /**
     * Returns the average bin number corresponding to a particular frequency.
     * Note: This function returns a float. Take the Math.round() of the
     * returned value to get an integral bin number.
     * 
     * @param samplingFrequency
     *            The Sampling Frequency of the AudioContext
     * @param blockSize
     *            The size of the fft block
     * @param freq
     *            The frequency
     */

    public static float binNumber(float samplingFrequency, int blockSize,
            float freq)
    {
        return blockSize * freq / samplingFrequency;
    }

    /**
     * The nyquist frequency for this samplingFrequency
     * 
     * @params samplingFrequency the sample
     */
    public static float nyquist(float samplingFrequency)
    {
        return samplingFrequency / 2;
    }
    
    /*
     * All of the code below this line is taken from Holger Crysandt's MPEG7AudioEnc project.
     * See http://mpeg7audioenc.sourceforge.net/copyright.html for license and copyright.
     */

    /**
     * Gets the real part from the complex spectrum.
     * 
     * @param spectrum
     *            complex spectrum.
     * @param length
     *            length of data to use.
     * 
     * @return real part of given length of complex spectrum.
     */
    protected static float[] calculateReal(float[] spectrum, int length) {
        float[] real = new float[length];
        real[0] = spectrum[0];
        real[real.length / 2] = spectrum[1];
        for (int i = 1, j = real.length - 1; i < j; ++i, --j)
            real[j] = real[i] = spectrum[2 * i];
        return real;
    }

    /**
     * Gets the imaginary part from the complex spectrum.
     * 
     * @param spectrum
     *            complex spectrum.
     * @param length
     *            length of data to use.
     * 
     * @return imaginary part of given length of complex spectrum.
     */
    protected static float[] calculateImaginary(float[] spectrum, int length) {
        float[] imag = new float[length];
        for (int i = 1, j = imag.length - 1; i < j; ++i, --j)
            imag[i] = -(imag[j] = spectrum[2 * i + 1]);
        return imag;
    }

    /**
     * Perform FFT on data with given length, regular or inverse.
     * 
     * @param data
     *            the data
     * @param n
     *            the length
     * @param isign
     *            true for regular, false for inverse.
     */
    protected static void fft(float[] data, int n, boolean isign) {
        float c1 = 0.5f;
        float c2, h1r, h1i, h2r, h2i;
        double wr, wi, wpr, wpi, wtemp;
        double theta = 3.141592653589793 / (n >> 1);
        if (isign) {
            c2 = -.5f;
            four1(data, n >> 1, true);
        } else {
            c2 = .5f;
            theta = -theta;
        }
        wtemp = Math.sin(.5 * theta);
        wpr = -2. * wtemp * wtemp;
        wpi = Math.sin(theta);
        wr = 1. + wpr;
        wi = wpi;
        int np3 = n + 3;
        for (int i = 2, imax = n >> 2, i1, i2, i3, i4; i <= imax; ++i) {
            /** @TODO this can be optimized */
            i4 = 1 + (i3 = np3 - (i2 = 1 + (i1 = i + i - 1)));
            --i4;
            --i2;
            --i3;
            --i1;
            h1i = c1 * (data[i2] - data[i4]);
            h2r = -c2 * (data[i2] + data[i4]);
            h1r = c1 * (data[i1] + data[i3]);
            h2i = c2 * (data[i1] - data[i3]);
            data[i1] = (float) (h1r + wr * h2r - wi * h2i);
            data[i2] = (float) (h1i + wr * h2i + wi * h2r);
            data[i3] = (float) (h1r - wr * h2r + wi * h2i);
            data[i4] = (float) (-h1i + wr * h2i + wi * h2r);
            wr = (wtemp = wr) * wpr - wi * wpi + wr;
            wi = wi * wpr + wtemp * wpi + wi;
        }
        if (isign) {
            float tmp = data[0];
            data[0] += data[1];
            data[1] = tmp - data[1];
        } else {
            float tmp = data[0];
            data[0] = c1 * (tmp + data[1]);
            data[1] = c1 * (tmp - data[1]);
            four1(data, n >> 1, false);
        }
    }

    /**
     * four1 algorithm.
     * 
     * @param data
     *            the data.
     * @param nn
     *            the nn.
     * @param isign
     *            regular or inverse.
     */
    private static void four1(float data[], int nn, boolean isign) {
        int n, mmax, istep;
        double wtemp, wr, wpr, wpi, wi, theta;
        float tempr, tempi;

        n = nn << 1;
        for (int i = 1, j = 1; i < n; i += 2) {
            if (j > i) {
                // SWAP(data[j], data[i]);
                float swap = data[j - 1];
                data[j - 1] = data[i - 1];
                data[i - 1] = swap;
                // SWAP(data[j+1], data[i+1]);
                swap = data[j];
                data[j] = data[i];
                data[i] = swap;
            }
            int m = n >> 1;
            while (m >= 2 && j > m) {
                j -= m;
                m >>= 1;
            }
            j += m;
        }
        mmax = 2;
        while (n > mmax) {
            istep = mmax << 1;
            theta = 6.28318530717959 / mmax;
            if (!isign)
                theta = -theta;
            wtemp = Math.sin(0.5 * theta);
            wpr = -2.0 * wtemp * wtemp;
            wpi = Math.sin(theta);
            wr = 1.0;
            wi = 0.0;
            for (int m = 1; m < mmax; m += 2) {
                for (int i = m; i <= n; i += istep) {
                    int j = i + mmax;
                    tempr = (float) (wr * data[j - 1] - wi * data[j]);
                    tempi = (float) (wr * data[j] + wi * data[j - 1]);
                    data[j - 1] = data[i - 1] - tempr;
                    data[j] = data[i] - tempi;
                    data[i - 1] += tempr;
                    data[i] += tempi;
                }
                wr = (wtemp = wr) * wpr - wi * wpi + wr;
                wi = wi * wpr + wtemp * wpi + wi;
            }
            mmax = istep;
        }
    }

}
