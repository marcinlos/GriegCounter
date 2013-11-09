package pl.edu.agh.ki.grieg.processing.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.model.CompositeModel;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.model.Models;
import pl.edu.agh.ki.grieg.model.SimpleModel;
import pl.edu.agh.ki.grieg.processing.core.ProcessingAdapter;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;
import pl.edu.agh.ki.grieg.util.math.Point;
import pl.edu.agh.ki.grieg.util.properties.Properties;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class WaveFunctionModel extends ProcessingAdapter implements
        Iteratee<float[]> {

    private final Logger logger = LoggerFactory
            .getLogger(WaveFunctionModel.class);

    private int rangeCount;

    private final List<Point> leftData;
    private final List<Point> rightData;

    private final SimpleModel<List<Point>> leftSerie;
    private final SimpleModel<List<Point>> rightSerie;

    private final List<SimpleModel<List<Point>>> series;

    private final CompositeModel<?> model;

    {
        leftData = Lists.newArrayList();
        rightData = Lists.newArrayList();

        leftSerie = Models.simple(leftData);
        rightSerie = Models.simple(rightData);

        series = ImmutableList.of(leftSerie, rightSerie);
        model = Models.container();
        model.addModel("left", leftSerie);
        model.addModel("right", rightSerie);
    }

    private int resolution;

    private final String sourceName;

    public WaveFunctionModel(String sourceName) {
        this.sourceName = sourceName;
    }

    protected Logger logger() {
        return logger;
    }

    protected float progress() {
        return rangeCount / (float) resolution;
    }

    @Override
    public void fileOpened(AudioFile file, Properties config) {
        resolution = config.getInt("resolution");
    }

    protected void sampleCountMissing() {
        logger.error("No sample count available, cannot create plot");
    }

    @Override
    public void beforeAnalysis(Pipeline<float[][]> pipeline,
            SampleEnumerator source) {
        pipeline.connect(this, float[].class).to(sourceName);
    }

    @Override
    public State step(float[] item) {
        ++rangeCount;
        float x = progress();
        for (int i = 0; i < item.length; ++i) {
            Point p = new Point(x, item[i]);
            Model<List<Point>> serie = series.get(i);
            List<Point> data = serie.getData();
            synchronized (data) {
                data.add(p);
            }
        }
        update();
        return State.Cont;
    }

    protected void update() {
        for (SimpleModel<List<Point>> serie : series) {
            serie.update();
        }
    }

    @Override
    public void finished() {
        reset();
    }

    @Override
    public void failed(Throwable e) {
        reset();
    }

    protected void reset() {
        rangeCount = 0;
    }

    public Model<?> getModel() {
        return model;
    }
}
