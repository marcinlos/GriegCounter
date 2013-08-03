package pl.edu.agh.ki.grieg.chart.swing.demo;

import java.awt.Color;
import java.util.List;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import pl.edu.agh.ki.grieg.chart.ChartModel;
import pl.edu.agh.ki.grieg.gfx.Point;

public class ChannelsChart extends JPanel {

    private final LineChartView top;
    private final LineChartView bottom;

    public ChannelsChart(ChartModel<List<Point>> model, float width,
            float height) {
        setBackground(Color.black);
        setLayout(new MigLayout("fill"));
        top = new LineChart(model.getSerie("top"), width, height);
        bottom = new LineChart(model.getSerie("bottom"), width, height);
        add(top.swingPanel(), "w 100%, h 50%, wrap");
        add(bottom.swingPanel(), "w 100%, h 50%");
    }

}
