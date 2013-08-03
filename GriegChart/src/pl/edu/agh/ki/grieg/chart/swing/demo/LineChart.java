package pl.edu.agh.ki.grieg.chart.swing.demo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import pl.edu.agh.ki.grieg.chart.Serie;
import pl.edu.agh.ki.grieg.chart.SerieListener;
import pl.edu.agh.ki.grieg.gfx.Point;

public class LineChart extends LineChartView implements
        SerieListener<List<Point>> {

    public LineChart(Serie<List<Point>> model, float width, float height) {
        super(width, height);
        checkNotNull(model);
        model.addListener(this);
        setData(model.getData());
    }

    @Override
    public void updated(Serie<List<Point>> serie) {
        refresh();
    }

}
