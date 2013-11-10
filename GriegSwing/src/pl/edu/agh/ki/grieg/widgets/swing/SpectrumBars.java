package pl.edu.agh.ki.grieg.widgets.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.model.Listener;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.swing.util.Utils;

public class SpectrumBars extends SwingCanvas implements Listener<float[]> {

    private float[] data;

    private double min = -100;
    private double max = 60;

    private final int barCount = 60;

    int count = 0;
    int redraws = 0;

    public SpectrumBars(Model<float[]> source) {
        JPanel p = swingPanel();
        p.setBackground(Color.black);
        source.addListener(this);
        p.setPreferredSize(new Dimension(100, 150));
    }

    @Override
    public void update(float[] data) {
        this.data = data.clone();
        refresh();
    }

    private int calcY(double value) {
        int h = getScreenHeight();
        double y = (value - min) / (max - min);
        return (int) (h * (1 - y));
    }

    private double minFreq = 20;

    double logFreq(double frequency) {
        return Math.log10(frequency / minFreq);
    }
    
    @Override
    protected void paint(Graphics2D graphics) {
        if (data != null) {
            double samplingFrequency = 44100;
            double nyquist = samplingFrequency / 2;
            int N = data.length;
            int K = N / 2 - 1;
            double freqBinSize = samplingFrequency / N;

            int screenWidth = getScreenWidth();
            int screenHeight = getScreenHeight();

            graphics.setColor(Color.white);
            double xmax = logFreq(nyquist);

            for (double f = minFreq; f < nyquist; f *= 2) {
                double x = logFreq(f) / xmax;
                int xpos = (int) (x * screenWidth);
                graphics.drawLine(xpos, screenHeight, xpos, 0);
            }

            int[] counts = new int[barCount];
            double[] bars = new double[barCount];
            for (int i = 1; i < K; ++i) {
                double f = i * freqBinSize;
                double x = logFreq(f) / xmax;
                int idx = (int) (x * barCount);
                if (idx >= 0) {
                    ++counts[idx];
                    bars[idx] += data[i];
                }
            }
            for (int i = 0; i < barCount; ++i) {
                int n = counts[i];
                double avg = n > 0 ? bars[i] / counts[i] : 0;
                double dB = Utils.clamp(Utils.dB(avg), min, max);
                bars[i] = (dB - min) / (max - min);
            }
            

            graphics.setColor(Color.green);
            for (int i = 1; i < K; ++i) {
                double f = i * freqBinSize;
                double x = logFreq(f) / xmax;

                double dB = Utils.clamp(Utils.dB(data[i]), min, max);

                int xpos = (int) (x * screenWidth);
                int ypos = calcY(dB);
                graphics.drawLine(xpos, screenHeight, xpos, ypos);
            }
            
            graphics.setColor(Color.white);
            double xscale = screenWidth / (double) barCount;
            int width = (int) (0.9 * screenWidth / barCount);
            for (int i = 0; i < barCount; ++ i) {
                int xpos = (int) (i * xscale);
                double y = bars[i];
                int ypos = (int) ((1 - y) * screenHeight);
                graphics.fillRect(xpos, ypos, width, screenHeight - ypos);
            }
        }
    }

}
