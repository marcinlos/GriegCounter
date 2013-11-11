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

public class SpectrumPanel extends JPanel implements Listener<float[]> {

    private float[] data;

    private double min = -100;
    private double max = 60;

    private double samplingFrequency = 44100;

    public SpectrumPanel(Model<float[]> source) {
        setBackground(Color.black);
        source.addListener(this);
        setPreferredSize(new Dimension(100, 150));
    }

    public void setSamplingFrequency(double frequency) {
        this.samplingFrequency = frequency;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data != null) {
            Graphics2D graphics = (Graphics2D) g;
            int N = data.length;
            int K = N / 2 - 1;
            double binWidth = 1.0 / K;
            double freqBinSize = samplingFrequency / N;
            double perHertz = binWidth / freqBinSize;

            graphics.setColor(Color.darkGray);
            for (int i = 0; i < samplingFrequency / 2; ++i) {
                double x = i * 1000 * perHertz;
                int xpos = (int) (x * getWidth());
                graphics.drawLine(xpos, getHeight(), xpos, 0);
            }

            graphics.setColor(Color.green);
            for (int i = 0; i < K; ++i) {
                double x = binWidth * i;

                double dB = clamp(dB(data[i]), min, max);

                int xpos = (int) (x * getWidth());
                int ypos = calcY(dB);
                graphics.drawLine(xpos, getHeight(), xpos, ypos);
            }
        }
    }

}
