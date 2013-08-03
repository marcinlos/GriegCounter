package pl.edu.agh.ki.grieg.chart.swing.demo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.chart.swing.SwingCanvas;
import pl.edu.agh.ki.grieg.gfx.Point;
import pl.edu.agh.ki.grieg.gfx.PointI;

public class LineChartView extends SwingCanvas {

    private List<Point> data;
    
    private final JPanel p = swingPanel();

    public LineChartView(float width, float height) {
        super(width, height);
        setupUI();
    }

    private void setupUI() {
        p.setBackground(Color.black);
        p.setBorder(BorderFactory.createLineBorder(Color.green, 2));
    }

    public void setData(List<Point> data) {
        this.data = checkNotNull(data);
        refresh();
    }

    public void clearData() {
        setData(null);
    }

    @Override
    protected void paint(Graphics2D graphics) {
        graphics.setColor(Color.green);
        if (data != null) {
            synchronized (data) {
                drawLines(graphics);
            }
        }
    }
    
    private PointI _(Point p) {
        return toScreen(p);
    }

    private void drawLines(Graphics2D graphics) {
        if (!data.isEmpty()) {
            Iterator<Point> it = data.iterator();
            Point a = it.next();
            while (it.hasNext()) {
                Point b = it.next();
                PointI p = _(a), q = _(b);
                graphics.drawLine(p.x, p.y, q.x, q.y);
                a = b;
            }
        }
    }
}
