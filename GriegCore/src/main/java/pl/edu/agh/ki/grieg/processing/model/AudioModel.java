package pl.edu.agh.ki.grieg.processing.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.model.CompositeModel;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.model.Models;
import pl.edu.agh.ki.grieg.model.SimpleModel;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.iteratee.State;
import pl.edu.agh.ki.grieg.util.math.Point;
import pl.edu.agh.ki.grieg.util.properties.Properties;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class AudioModel extends WaveObserver {

    private static final Logger logger = LoggerFactory
            .getLogger(AudioModel.class);

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

    public AudioModel() {
        logger.trace("Audio model created");
    }

    @Override
    protected void sampleCountMissing() {
        logger.error("No sample count available, cannot display wave");
    }

    @Override
    public void fileOpened(AudioFile file, Properties config) {
        super.fileOpened(file, config);
        logger.trace("File opened: {}", file);
        clear();
    }

    @Override
    public void beforePreAnalysis(ExtractionContext ctx) {
        super.beforePreAnalysis(ctx);
        logger.trace("Before reading metainfo");
    }

    @Override
    public void afterPreAnalysis(Properties results) {
        super.afterPreAnalysis(results);
        logger.trace("Metadata reading finished, waiting for sound data");
    }

    @Override
    public void beforeAnalysis(Pipeline<float[][]> pipeline) {
        super.beforeAnalysis(pipeline);
        logger.trace("Processing has started");
    }

    @Override
    public State step(float[] item) {
        super.step(item);
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

    private void update() {
        for (SimpleModel<List<Point>> serie : series) {
            serie.update();
        }
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

    @Override
    public void finished() {
        logger.trace("Done processing");
        super.finished();
    }

    @Override
    public void failed(Throwable e) {
        // This should be logged by some higher-level code
        // logger.error("Failure", e);
        super.failed(e);
    }

    public Model<?> getModel() {
        return model;
    }

}
