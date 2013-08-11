package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.Properties;

/**
 * Empty implementation of {@link ProcessingListener}, added for convenience of
 * users, who often don'st need all the hooks.
 * 
 * @author los
 */
public class ProcessingAdapter implements ProcessingListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void fileOpened(AudioFile file, Properties config) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforePreAnalysis(ExtractionContext ctx) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPreAnalysis(Properties results) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeAnalysis(Pipeline<float[][]> pipeline) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterAnalysis() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failed(Throwable e) {
        // empty
    }

}
