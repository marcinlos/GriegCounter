package pl.edu.agh.ki.grieg.gui;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.processing.observers.PCMObserver;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.Key;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.Range;
import pl.edu.agh.ki.grieg.util.iteratee.State;

public class WavePresenter extends PCMObserver {

    private static final Logger logger = LoggerFactory
            .getLogger(WavePresenter.class);
    
    private final WaveView view;

    public WavePresenter(WaveView view) {
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
        view.reset();
    }
    
    StaticLoggerBinder a;

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
    public void beforeAnalysis(Pipeline<float[][]> pipeline) {
        super.beforeAnalysis(pipeline);
        logger.trace("Processing has started");
    }
    
    @Override
    public State step(Range[] item) {
        super.step(item);
        view.drawRange(progress(), item);
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
