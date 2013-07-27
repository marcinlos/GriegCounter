package pl.edu.agh.ki.grieg.gui;

import java.util.Arrays;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.processing.observers.PCMObserver;
import pl.edu.agh.ki.grieg.processing.observers.WaveObserver;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.swing.graphics.Point;
import pl.edu.agh.ki.grieg.utils.Key;
import pl.edu.agh.ki.grieg.utils.Properties;
import pl.edu.agh.ki.grieg.utils.Range;
import pl.edu.agh.ki.grieg.utils.iteratee.State;

public class WavePresenter2 extends WaveObserver {

    private static final Logger logger = LoggerFactory
            .getLogger(WavePresenter2.class);
    
    private final WaveView2 view;
    
    private Point[] prev = new Point[2]; 
    {
        Arrays.fill(prev, Point.ORIGIN);
    }

    public WavePresenter2(WaveView2 view) {
        this.view = view;
        logger.trace("Wave presenter created, using view = {}", view);
    }

    @Override
    protected void sampleCountMissing() {
        logger.error("No sample count available, cannot display wave");
    }

    @Override
    public void fileOpened(AudioFile file) {
        super.fileOpened(file);
        logger.trace("File opened: {}", file);
    }

    @Override
    public void beforePreAnalysis(Set<Key<?>> desired, Properties config) {
        super.beforePreAnalysis(desired, config);
        logger.trace("Before reading metainfo");
    }

    @Override
    public void afterPreAnalysis(Properties results) {
        super.afterPreAnalysis(results);
        logger.trace("Metadata reading finished, waiting for sound data");
    }

    @Override
    public void beforeAnalysis(Pipeline<float[][]> tree) {
        super.beforeAnalysis(tree);
        logger.trace("Processing has started");
    }
    
    @Override
    public State step(float[] item) {
        super.step(item);
        float x = progress();
        for (int i = 0; i < item.length; ++ i) {
            Point end = new Point(progress(), item[i]);
            view.drawLine(i, prev[i], end);
            prev[i] = end;
        }
        view.repaint();
        return State.Cont;
    }
    
    @Override
    public void finished() {
        logger.trace("Done processing");
        view.repaint();
    }

    @Override
    public void failed(Throwable e) {
        logger.error("Failure", e);
    }

}
