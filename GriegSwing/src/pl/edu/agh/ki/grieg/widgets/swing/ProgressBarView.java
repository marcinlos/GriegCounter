package pl.edu.agh.ki.grieg.widgets.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ProgressBarView extends JPanel {

    private float value = 0.0f;
    
    public ProgressBarView() {
        setupUI();
    }
    
    private void setupUI() {
        setBackground(Color.black);
        setBorder(BorderFactory.createLineBorder(Color.gray, 1));
    }

    public void setData(float value) {
        this.value = value;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        graphics.setColor(Color.green);
        
        int w = getWidth();
        int h = getHeight();
        
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
