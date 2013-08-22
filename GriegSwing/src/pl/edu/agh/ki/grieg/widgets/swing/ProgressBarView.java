package pl.edu.agh.ki.grieg.widgets.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ProgressBarView extends SwingCanvas {

    private final JPanel p = swingPanel();
    
    private float value = 0.0f;
    
    public ProgressBarView() {
        setupUI();
    }
    
    private void setupUI() {
        p.setBackground(Color.black);
        p.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
    }

    public void setData(float value) {
        this.value = value;
        refresh();
    }

    @Override
    protected void paint(Graphics2D graphics) {
        graphics.setColor(Color.green);
        
        int w = getScreenWidth();
        int h = getScreenHeight();
        
        int rw = (int) (w * value);

        float[] points = {0f, 0.5f, 1f};
        Color[] colors = {Color.GREEN, Color.YELLOW, Color.RED};
        graphics.setPaint(new LinearGradientPaint(0, 0, w, 0, points, colors));
        
        graphics.fillRoundRect(1, 1, rw, h, 4, 4);

        // Border
        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(1, 1, w, h, 4, 4);
    }

}
