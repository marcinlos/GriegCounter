package griegcounter.test.gui;

import griegcounter.graphics.AbstractDrawable;
import griegcounter.graphics.Point;

import java.awt.Color;
import java.awt.Graphics2D;

public class DrawableGraphics2d extends AbstractDrawable {
    
    private Graphics2D g;
    private float width;
    private float height;

    public DrawableGraphics2d(Graphics2D g, int width, int height) {
        this.g = g;
        this.width = width;
        this.height = height;
        g.setColor(Color.green);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void line(Point begin, Point end) {
        begin = transform(begin);
        end = transform(end);
        int x1 = (int) begin.x;
        int y1 = (int) begin.y;
        int x2 = (int) end.x;
        int y2 = (int) end.y;
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void debugText(String text, Point pos) {
        pos = transform(pos);
        g.drawString(text, (int) pos.x, (int) pos.y);
    }

    public Graphics2D getGraphics() {
        return g;
    }
    
}
