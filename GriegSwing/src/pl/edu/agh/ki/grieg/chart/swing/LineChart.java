package pl.edu.agh.ki.grieg.chart.swing;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import pl.edu.agh.ki.grieg.model.observables.Listener;
import pl.edu.agh.ki.grieg.model.observables.Model;
import pl.edu.agh.ki.grieg.util.Point;

public class LineChart extends LineChartView implements
        Listener<List<Point>> {

    public LineChart(Model<List<Point>> serie, float width, float height) {
        super(width, height);
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
