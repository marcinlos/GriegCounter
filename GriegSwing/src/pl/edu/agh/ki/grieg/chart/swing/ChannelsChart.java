package pl.edu.agh.ki.grieg.chart.swing;

import java.awt.Color;
import java.util.List;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.util.Point;
import pl.edu.agh.ki.grieg.util.Reflection;

public class ChannelsChart extends JPanel {

    private final LineChart left;
    private final LineChart right;

    public ChannelsChart(Model<?> model, float width, float height) {
        setBackground(Color.black);
        setLayout(new MigLayout("fill"));

        Class<? extends List<Point>> clazz = Reflection.castClass(List.class);
        Model<List<Point>> leftSerie = model.getChild("left", clazz);
        Model<List<Point>> rightSerie = model.getChild("right", clazz);

        left = new LineChart(leftSerie, width, height);
        right = new LineChart(rightSerie, width, height);
        add(left.swingPanel(), "w 100%, h 50%, wrap");
        add(right.swingPanel(), "w 100%, h 50%");
    }

}
