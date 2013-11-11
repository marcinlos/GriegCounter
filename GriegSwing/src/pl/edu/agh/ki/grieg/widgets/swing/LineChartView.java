package pl.edu.agh.ki.grieg.widgets.swing;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.util.math.Point;
import pl.edu.agh.ki.grieg.util.math.PointI;

public class LineChartView extends SwingCanvas {

    private List<Point> data;
    
    private float min;

    private final JPanel p = swingPanel();

    public LineChartView(double width, double min, double max) {
        super(width, max - min);
        this.min = (float) min;
        setupUI();
    }

    private void setupUI() {
        p.setBackground(Color.black);
        p.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
    }

    public void setData(List<Point> data) {
        this.data = checkNotNull(data);
        refresh();
    }

    public void clearData() {
        List<Point> empty = Collections.emptyList();
        setData(empty);
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

    private void drawLines(Graphics2D graphics) {
        int n = data.size();
        int[] xs = new int[n];
        int[] ys = new int[n];
        for (int i = 0; i < n; ++ i) {
            Point p = data.get(i);
            Point lowered = new Point(p.x, p.y - min);
            PointI transformed = toScreen(lowered);
            xs[i] = transformed.x;
            ys[i] = transformed.y;
        }
        graphics.drawPolyline(xs, ys, n);
    }
}
