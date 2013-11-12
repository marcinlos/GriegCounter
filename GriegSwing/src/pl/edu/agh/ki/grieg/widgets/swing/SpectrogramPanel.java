package pl.edu.agh.ki.grieg.widgets.swing;

import static pl.edu.agh.ki.grieg.swing.util.Utils.clamp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.model.Listener;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.swing.util.Colors;

public class SpectrogramPanel extends JPanel implements Listener<float[]> {

    private static final int WIDTH = 4096;
    private static final int HEIGHT = 512;

    private static final int GAP = 5;

    private double min = -10;
    private double max = 60;

    private final double minFrequency = 20;
    private double samplingFrequency = 44100;

    private int column = 0;
    private boolean reachedEdge = false;

    private static final Color[] colors;
    private static final int N = 256;

    static {
        colors = new Color[N + 1];
        for (int i = 0; i <= N; ++i) {
            double t = i / (double) N;
            float a = (float) (t * t);
            float s = (float) Math.PI;
            colors[i] = Colors.hsv(s * (1.4f + a), 1, a, 1);
        }
    }

    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
            BufferedImage.TYPE_INT_ARGB);

    private final Executor worker = Executors.newSingleThreadExecutor();

    public SpectrogramPanel(Model<float[]> source) {
        source.addListener(this);
        setBackground(Color.black);
    }

    public void setSamplingFrequency(double frequency) {
        this.samplingFrequency = frequency;
    }

    private double logFreq(double frequency) {
        return Math.log10(frequency / minFrequency);
    }

    private double dB(double a) {
        return 10 * Math.log10(a);
    }

    private Color green(double alpha) {
        int n = (int) (alpha * N);
        return colors[n > N ? N : n];
    }

    @Override
    public void update(float[] data) {
        final float[] copy = data.clone();

        worker.execute(new Runnable() {
            @Override
            public void run() {
                process(copy);
            }
        });
    }

    private void process(float[] data) {
        double nyquist = samplingFrequency / 2;
        int N = data.length;
        int K = N / 2 - 1;
        double freqBinSize = samplingFrequency / N;

//        double xmax = logFreq(nyquist);

        synchronized (image) {

            double[] vals = new double[HEIGHT];

            for (int i = 1; i < K; i += GAP) {
                double f = i * freqBinSize;
                double y = f / nyquist;
                // double y = clamp(logFreq(f) / xmax, 0.1, 0.9);
                int ypos = (int) (HEIGHT * (1 - y));

                double dB = clamp(dB(data[i]), min, max);
                double t = (dB - min) / (max - min);
                // alpha-blending
                vals[ypos] = t + (1 - t) * vals[ypos];
            }
            int[] rgbArray = new int[HEIGHT];
            for (int i = 0; i < HEIGHT; ++i) {
                rgbArray[i] = green(vals[i]).getRGB();
            }
            image.setRGB(column, 0, 1, HEIGHT, rgbArray, 0, 1);
            column = (column + 1) % WIDTH;
            if (column == 0) {
                reachedEdge = true;
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        double split = column / (double) WIDTH;
        synchronized (image) {
            if (!reachedEdge) {
                g.drawImage(image, 0, 0, width, height, null);
            } else {
                int mid = (int) ((1 - split) * width);
                g.drawImage(image, 0, 0, mid, height, column, 0, WIDTH, HEIGHT,
                        null);
                g.drawImage(image, mid, 0, width, height, 0, 0, column, HEIGHT,
                        null);
            }
        }
    }

}
