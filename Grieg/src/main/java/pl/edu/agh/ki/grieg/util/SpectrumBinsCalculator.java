package pl.edu.agh.ki.grieg.util;

/**
 * Calculates average intensities (in dB) of signal given in frequency domain
 * (e.g. output of FFT of regular, time-domain signal) in logarithmically sized
 * bins.
 * 
 * <p>
 * Specifically, given input {@code a_0,...,a_n} and sampling frequency
 * {@code f}, each {@code a_i} is understood as coefficient of {@code i}-th
 * frequency ({@code i * samplingFrequency / numberOfSamples}, and placed in an
 * appropriate bin (one of {@code binCount}), based on the logarithm of
 * frequency.
 * 
 * <p>
 * This class is ment as a helper for creating a signal spectrum visualization.
 * 
 * @author los
 */
public class SpectrumBinsCalculator {

    /** Minimal considered frequency, to avoid gap after zero */
    private final double minFrequency;

    /** Number of bins to split frequencies into */
    private final int binCount;

    /**
     * Creates a {@code SpectrumBinsCalculator} for specified bin count and
     * minimal frequency.
     * 
     * @param binCount
     *            Number of beans
     * @param minFrequency
     *            Minimal considered frequency
     */
    public SpectrumBinsCalculator(int binCount, double minFrequency) {
        this.binCount = binCount;
        this.minFrequency = minFrequency;
    }

    /**
     * Converts frequency to logarithmic form
     */
    private double logFreq(double frequency) {
        return Math.log10(frequency / minFrequency);
    }

    /**
     * Calculates intensity in {@code dB}.
     * 
     * @param a
     *            Value in a regular scale
     * @return Value in {@code dB}
     */
    private double dB(double a) {
        return 10 * Math.log10(a);
    }

    /**
     * Computes average intensity of frequencies in input data in each bin.
     * 
     * <p>
     * Note: the input is assumed to be a power spectrum of real wave, and so
     * only frequencies up to Nyquist frequency are considred.
     * 
     * @param data
     *            Signal in frequency domain
     * @param samplingRate
     *            Sampling rate of the signal
     * @return Array of average intensity in each bin
     */
    public double[] compute(float[] data, double samplingRate) {
        int N = data.length;
        int K = N / 2 - 1;
        double freqBinSize = samplingRate / N;
        double nyquist = samplingRate / 2;
        double logFmax = logFreq(nyquist);

        int[] counts = new int[binCount];
        double[] bins = new double[binCount];
        for (int i = 1; i < K; ++i) {
            double f = i * freqBinSize;
            double x = logFreq(f) / logFmax;
            int idx = (int) (x * binCount);
            if (idx >= 0) {
                ++counts[idx];
                bins[idx] += data[i];
            }
        }
        for (int i = 0; i < binCount; ++i) {
            int n = counts[i];
            double avg = n > 0 ? bins[i] / counts[i] : 0;
            bins[i] = dB(avg);
        }
        return bins;
    }
}