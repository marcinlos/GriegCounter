package pl.edu.agh.ki.grieg.widgets.swing;

import static pl.edu.agh.ki.grieg.swing.util.Utils.clamp;
import static pl.edu.agh.ki.grieg.swing.util.Utils.dB;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.model.Listener;
import pl.edu.agh.ki.grieg.model.Model;

public class LogSpectrumPanel extends JPanel implements Listener<float[]> {

    private float[] data;

    private double min = -40;
    private double max = 60;
    
    private double samplingFrequency = 44100;

    public LogSpectrumPanel(Model<float[]> source) {
        setBackground(Color.black);
        source.addListener(this);
        setPreferredSize(new Dimension(100, 150));
    }

    @Override
    public void update(float[] data) {
        this.data = data.clone();
        repaint();
    }

    private int calcY(double value) {
        int h = getHeight();
        double y = (value - min) / (max - min);
        return (int) (h * (1 - y));
    }

    private double minFreq = 20;

    private double logFreq(double frequency) {
        return Math.log10(frequency / minFreq);
    }
    
    public void setSamplingFrequency(double frequency) {
        this.samplingFrequency = frequency;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data != null) {
            Graphics2D graphics = (Graphics2D) g;
            double nyquist = samplingFrequency / 2;
            int N = data.length;
            int K = N / 2 - 1;
            double freqBinSize = samplingFrequency / N;

            int screenWidth = getWidth();
            int screenHeight = getHeight();

            graphics.setColor(Color.white);
            double xmax = logFreq(nyquist);

            graphics.setColor(Color.green);
            for (int i = 1; i < K; ++i) {
                double f = i * freqBinSize;
                double x = logFreq(f) / xmax;

                double dB = clamp(dB(data[i]), min, max);

                int xpos = (int) (x * screenWidth);
                int ypos = calcY(dB);
                graphics.drawLine(xpos, screenHeight, xpos, ypos);
            }

        }
    }
}
