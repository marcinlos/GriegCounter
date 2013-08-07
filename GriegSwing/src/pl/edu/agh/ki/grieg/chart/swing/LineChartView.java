package pl.edu.agh.ki.grieg.chart.swing;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.util.Point;
import pl.edu.agh.ki.grieg.util.PointI;

public class LineChartView extends SwingCanvas {

    private List<Point> data;

    private final JPanel p = swingPanel();

    public LineChartView(float width, float height) {
        super(width, height);
        setupUI();
    }

    private void setupUI() {
        p.setBackground(Color.black);
        p.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
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

    private void drawLines(Graphics2D graphics) {
        for (int i = 1; i < data.size(); ++i) {
            Point a = data.get(i - 1), b = data.get(i);
            Point aa = new Point(a.x, a.y + 1);
            Point bb = new Point(b.x, b.y + 1);
            PointI p = toScreen(aa), q = toScreen(bb);
            graphics.drawLine(p.x, p.y, q.x, q.y);
        }
    }
}
