package pl.edu.agh.ki.grieg.processing.core;

import java.util.Set;

import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.utils.Key;
import pl.edu.agh.ki.grieg.utils.Properties;

public class ProcessingAdapter implements ProcessingListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void fileOpened(AudioFile file) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readingMetaInfo(Set<Key<?>> desired, Properties config) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gatheredMetainfo(Properties info) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processingStarted(Pipeline<float[][]> tree) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failed(Exception e) {
        // empty
    }

}
