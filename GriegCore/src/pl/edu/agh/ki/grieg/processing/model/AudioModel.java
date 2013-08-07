package pl.edu.agh.ki.grieg.processing.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.meta.ExtractionContext;
import pl.edu.agh.ki.grieg.model.Chart;
import pl.edu.agh.ki.grieg.model.ChartModel;
import pl.edu.agh.ki.grieg.model.Serie;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.Point;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.iteratee.State;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class AudioModel extends WaveObserver {

    private static final Logger logger = LoggerFactory
            .getLogger(AudioModel.class);

    private final List<Point> leftData;
    private final List<Point> rightData;

    private final Serie<List<Point>> leftSerie;
    private final Serie<List<Point>> rightSerie;

    private final List<Serie<List<Point>>> series;

    private final ChartModel<List<Point>> model;

    {
        leftData = Lists.newArrayList();
        rightData = Lists.newArrayList();

        leftSerie = Serie.of(leftData);
        rightSerie = Serie.of(rightData);

        series = ImmutableList.of(leftSerie, rightSerie);

        model = Chart.create();
        model.add("left", leftSerie);
        model.add("right", rightSerie);
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
            Serie<List<Point>> serie = series.get(i);
            List<Point> data = serie.getData();
            synchronized (data) {
                data.add(p);
            }
        }
        update();
        return State.Cont;
    }

    private void update() {
        for (Serie<List<Point>> serie : series) {
            serie.signalChange();
        }
    }

    public ChartModel<List<Point>> getChartModel() {
        return model;
    }

    private void clear() {
        for (Serie<List<Point>> serie : series) {
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

}
