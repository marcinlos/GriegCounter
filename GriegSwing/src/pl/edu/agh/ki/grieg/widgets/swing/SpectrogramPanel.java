package pl.edu.agh.ki.grieg.widgets.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.model.Listener;

public class SpectrogramPanel extends JPanel implements Listener<float[]> {

    private final int WIDTH = 2048;
    private final int HEIGHT = 512;
    
    private double min = -40;
    private double max = 60;

    private final double minFrequency = 20;

    private final Image image = new BufferedImage(WIDTH, HEIGHT,
            BufferedImage.TYPE_INT_ARGB);

    public SpectrogramPanel() {
        setBackground(Color.black);

    }

    private double logFreq(double frequency) {
        return Math.log10(frequency / minFrequency);
    }

    private double dB(double a) {
        return 10 * Math.log10(a);
    }
    
    private Color green(double alpha) {
        return new Color(0, 1, 0, (float) alpha);
    }

    @Override
    public void update(float[] data) {
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.red);
        g.fillRect(10, 10, 2028, 492);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }

}
