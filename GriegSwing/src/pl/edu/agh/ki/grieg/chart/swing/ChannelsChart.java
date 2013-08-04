package pl.edu.agh.ki.grieg.chart.swing;

import java.awt.Color;
import java.util.List;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import pl.edu.agh.ki.grieg.chart.ChartModel;
import pl.edu.agh.ki.grieg.chart.Serie;
import pl.edu.agh.ki.grieg.gfx.Point;

public class ChannelsChart extends JPanel {

    private final LineChart left;
    private final LineChart right;

    public ChannelsChart(ChartModel<List<Point>> model, float width, float height) {
        setBackground(Color.black);
        setLayout(new MigLayout("fill"));
        
        Serie<List<Point>> leftSerie = model.getSerie("left");
        Serie<List<Point>> rightSerie = model.getSerie("right");

        left = new LineChart(leftSerie, width, height);
        right = new LineChart(rightSerie, width, height);
        add(left.swingPanel(), "w 100%, h 50%, wrap");
        add(right.swingPanel(), "w 100%, h 50%");
    }
    
}
