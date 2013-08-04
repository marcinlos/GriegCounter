package pl.edu.agh.ki.grieg.chart.swing;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import pl.edu.agh.ki.grieg.model.Serie;
import pl.edu.agh.ki.grieg.model.SerieListener;
import pl.edu.agh.ki.grieg.util.Point;

public class LineChart extends LineChartView implements
        SerieListener<List<Point>> {

    public LineChart(Serie<List<Point>> serie, float width, float height) {
        super(width, height);
        checkNotNull(serie);
        serie.addListener(this);
        setData(serie.getData());
        refresh();
    }


    @Override
    public void updated(Serie<List<Point>> serie) {
        refresh();
    }

}
