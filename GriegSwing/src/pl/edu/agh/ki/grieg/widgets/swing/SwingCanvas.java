package pl.edu.agh.ki.grieg.widgets.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.gfx.AbstractCanvas;

public class SwingCanvas extends AbstractCanvas {
    
    private final class SwingPanelAdapter extends JPanel {
        @Override
        protected final void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics = (Graphics2D) g;
            SwingCanvas.this.paint(graphics);
        }
    }

    private final JPanel swingPanel = new SwingPanelAdapter();

    public SwingCanvas() {
        // empty
    }

    public SwingCanvas(float width, float height) {
        super(width, height);
    }
    
    public final JPanel swingPanel() {
        return swingPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh() {
        swingPanel.repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getScreenWidth() {
        return swingPanel.getWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getScreenHeight() {
        return swingPanel.getHeight();
    }
    
    protected void paint(Graphics2D graphics) {
        // empty
    }

}
