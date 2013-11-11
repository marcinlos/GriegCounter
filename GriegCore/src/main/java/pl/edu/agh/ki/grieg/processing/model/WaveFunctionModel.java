package pl.edu.agh.ki.grieg.processing.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.processing.core.ProcessingListener;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;
import pl.edu.agh.ki.grieg.util.math.Point;
import pl.edu.agh.ki.grieg.util.properties.Properties;

import com.google.common.collect.Lists;

public class WaveFunctionModel extends AbstractChannelModel<List<Point>>
        implements Iteratee<float[]>, ProcessingListener {

    private final Logger logger = LoggerFactory
            .getLogger(WaveFunctionModel.class);

    private int rangeCount;

    private final List<Point> leftData;
    private final List<Point> rightData;

    {
        leftData = Lists.newArrayList();
        rightData = Lists.newArrayList();
    }

    private int resolution;

    private final String sourceName;

    public WaveFunctionModel(String sourceName) {
        super(Reflection.<List<Point>> castClass(List.class));
        this.sourceName = sourceName;
        leftSerie.update(leftData);
        rightSerie.update(rightData);
    }

    protected Logger logger() {
        return logger;
    }

    protected float progress() {
        return rangeCount / (float) resolution;
    }

    @Override
    public void fileOpened(AudioFile file, Properties config) {
        clear();
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

    @Override
    public void finished() {
        reset();
    }

    @Override
    public void failed(Throwable e) {
        reset();
    }

    @Override
    public void beforePreAnalysis(ExtractionContext ctx) {
        // empty
    }

    @Override
    public void afterPreAnalysis(Properties results) {
        // empty
    }

    @Override
    public void afterAnalysis() {
        // empty
    }

    protected void reset() {
        rangeCount = 0;
    }

    private void clear() {
        for (Model<List<Point>> serie : series) {
            List<Point> data = serie.getData();
            synchronized (data) {
                data.clear();
            }
        }
        update();
    }

}
