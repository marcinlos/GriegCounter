package pl.edu.agh.ki.grieg.widgets.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.model.Listener;
import pl.edu.agh.ki.grieg.model.Model;

public class SpectrumPanel extends SwingCanvas implements Listener<float[]> {

    private float[] data;
    
    private double min = - 10;
    private double max = 30;

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
    
    private int calcY(float value) {
        int h = getScreenHeight();
        double y = (value - min) / (max - min);
        return (int) (h * (1 - y));
    }

    @Override
    protected void paint(Graphics2D graphics) {
        if (data != null) {
            double w = 1.0 / data.length;
            
            graphics.setColor(Color.green);
            for (int i = 0; i < data.length; ++ i) {
                double x = w * i;
                
                int xpos = (int) (x * getScreenWidth());
                int ypos = calcY(data[i]); 
                graphics.drawLine(xpos, getScreenHeight(), xpos, ypos);
            }
        }
    }

}
