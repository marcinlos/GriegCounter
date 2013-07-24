package pl.edu.agh.ki.grieg.gui;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.processing.observers.PCMObserver;
import pl.edu.agh.ki.grieg.processing.tree.ProcessingTree;
import pl.edu.agh.ki.grieg.utils.Key;
import pl.edu.agh.ki.grieg.utils.Properties;

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
    }

    @Override
    public void readingMetaInfo(Set<Key<?>> desired, Properties config) {
        super.readingMetaInfo(desired, config);
        logger.trace("Before reading metainfo");
    }

    @Override
    public void gatheredMetainfo(Properties info) {
        super.gatheredMetainfo(info);
        logger.trace("Metadata reading finished, waiting for sound data");
    }

    @Override
    public void processingStarted(ProcessingTree<float[][]> tree) {
        super.processingStarted(tree);
        logger.trace("Processing has started");
    }

    @Override
    public void failed(Exception e) {
        logger.error("Failure", e);
    }

}
