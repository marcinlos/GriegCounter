package pl.edu.agh.ki.grieg.widgets.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.model.Listener;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.util.SpectrumBinsCalculator;

public class SpectrumBars extends SwingCanvas implements Listener<float[]> {

    private float[] data;

    private double min = -40;
    private double max = 60;

    private final int barCount = 60;

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

    private double minFreq = 20;

    @Override
    protected void paint(Graphics2D graphics) {
        if (data != null) {
            double samplingFrequency = 44100;

            int screenWidth = getScreenWidth();
            int screenHeight = getScreenHeight();

            SpectrumBinsCalculator calc = new SpectrumBinsCalculator(barCount,
                    minFreq);
            double[] bars = calc.compute(data, samplingFrequency);

            graphics.setColor(Color.green);
            double xscale = screenWidth / (double) barCount;
            
            for (int i = 0; i < barCount; ++i) {
                int xpos = (int) (i * xscale);
                int xnext = (int) ((i + 1) * xscale);
                int width = xnext - xpos - 1;
                
                double y = (bars[i] - min) / (max - min);
                int ypos = (int) ((1 - y) * screenHeight);
                graphics.fillRect(xpos, ypos, width, screenHeight - ypos);
            }
        }
    }

}
