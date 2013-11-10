package pl.edu.agh.ki.grieg.widgets.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.model.Listener;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.swing.util.Utils;

public class SpectrumPanel extends SwingCanvas implements Listener<float[]> {

    private float[] data;

    private double min = -140;
    private double max = 120;

    int count = 0;
    int redraws = 0;

    public SpectrumPanel(Model<float[]> source) {
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

    @Override
    protected void paint(Graphics2D graphics) {
        if (data != null) {
            double samplingFrequency = 44100;
            int N = data.length;
            int K = N / 2 - 1;
            double binWidth = 1.0 / K;
            double freqBinSize = samplingFrequency / N;
            double perHertz = binWidth / freqBinSize;
            
            graphics.setColor(Color.white);
            for (int i = 0; i < samplingFrequency / 2; ++ i) {
                double x = i * 1000 * perHertz;
                int xpos = (int) (x * getScreenWidth());
                graphics.drawLine(xpos, getScreenHeight(), xpos, 0);
            }

            graphics.setColor(Color.green);
            for (int i = 0; i < K; ++i) {
                double x = binWidth * i;

                double dB = Utils.clamp(Utils.dB(data[i]*data[i]), min, max);

                int xpos = (int) (x * getScreenWidth());
                int ypos = calcY(dB);
                graphics.drawLine(xpos, getScreenHeight(), xpos, ypos);
            }
        }
    }

}
