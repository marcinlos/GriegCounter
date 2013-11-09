package pl.edu.agh.ki.grieg.widgets.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.math.Point;

public class ChannelsChart extends JPanel {

    private final LineChart left;
    private final LineChart right;

    public ChannelsChart(String title, Model<?> model, double width,
            double min, double max) {
        setLayout(new BorderLayout());
        setBackground(Color.black);

        Class<? extends List<Point>> clazz = Reflection.castClass(List.class);
        Model<List<Point>> leftSerie = model.getChild("left", clazz);
        Model<List<Point>> rightSerie = model.getChild("right", clazz);

        left = new LineChart(leftSerie, width, min, max);
        right = new LineChart(rightSerie, width, min, max);

        
        JLabel label = new JLabel(title);
        label.setForeground(Color.green);
        add(label, BorderLayout.PAGE_START);
        
        JPanel charts = new JPanel(new MigLayout("fill"));
        charts.setBackground(Color.black);
        add(charts);

        charts.add(left.swingPanel(), "w 100%, h 50%, wrap");
        charts.add(right.swingPanel(), "w 100%, h 50%");
    }

}
