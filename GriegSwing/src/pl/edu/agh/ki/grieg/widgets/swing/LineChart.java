package pl.edu.agh.ki.grieg.widgets.swing;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import pl.edu.agh.ki.grieg.model.Listener;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.util.math.Point;

public class LineChart extends LineChartView implements
        Listener<List<Point>> {

    public LineChart(Model<List<Point>> serie, double width, double min,
            double max) {
        super(width, min, max);
        checkNotNull(serie);
        serie.addListener(this);
        setData(serie.getData());
        refresh();
    }

    @Override
    public void update(List<Point> data) {
        refresh();
    }

}
