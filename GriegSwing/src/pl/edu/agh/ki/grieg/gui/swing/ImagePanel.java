package pl.edu.agh.ki.grieg.gui.swing;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.swing.graphics.Drawable;
import pl.edu.agh.ki.grieg.swing.test.gui.DrawableGraphics2d;

public class ImagePanel extends JPanel {

    private BufferedImage image;

    public ImagePanel() {
        // TODO Auto-generated constructor stub
    }

    public boolean hasImage() {
        return image != null;
    }

    public void refresh(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void refresh() {
        refresh(getWidth(), getHeight());
    }

    public Graphics2D getGraphics() {
        checkNotNull(image, "Cannot obtain Graphics for null image");
        return image.createGraphics();
    }
    
    public Drawable getDrawable() {
        Graphics2D g = getGraphics();
        g.setColor(Color.black);
        return new DrawableGraphics2d(g, getWidth(), getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        if (hasImage()) {
            int imgWidth = image.getWidth();
            int imgHeight = image.getHeight();
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            graphics.drawImage(image, 
                0, 0, panelWidth, panelHeight, 
                0, 0, imgWidth, imgHeight, null);
        }
    }

}
