package pl.edu.agh.ki.grieg.swing.test.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.swing.graphics.Drawable;

class ImagePanel extends JPanel {

    private BufferedImage image;

    public void refresh() {
        image = new BufferedImage(getWidth(), getHeight(),
                BufferedImage.TYPE_INT_ARGB);
    }
    
    public boolean hasImage() {
        return image != null;
    }
    
    public Drawable getDrawable() {
        if (hasImage()) {
            Graphics2D g = image.createGraphics();
            g.setColor(Color.black);
            return new DrawableGraphics2d(g, getWidth(), getHeight());
        } else {
            throw new IllegalStateException("No image");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }
}