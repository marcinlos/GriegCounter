package pl.edu.agh.ki.grieg.widgets.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.model.Listener;
import pl.edu.agh.ki.grieg.model.Model;

public class SpectrumPanel extends SwingCanvas implements Listener<float[]> {

    private float[] data;

    private double min = -100;
    private double max = 60;

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

    private static double dB(double val) {
        return 10 * Math.log10(val);
    }

    private int calcY(double value) {
        int h = getScreenHeight();
        double y = (value - min) / (max - min);
        return (int) (h * (1 - y));
    }

    @Override
    protected void paint(Graphics2D graphics) {
        if (data != null) {
            int N = data.length / 2;
            double binSize = 1.0 / N;

            graphics.setColor(Color.green);
            for (int i = 0; i < N; ++i) {
                double x = binSize * i;

                double dB = dB(data[i]);

                int xpos = (int) (x * getScreenWidth());
                int ypos = calcY(dB);
                graphics.drawLine(xpos, getScreenHeight(), xpos, ypos);
            }
        }
    }

}
