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
    public void beforePreAnalysis(Set<Key<?>> desired, Properties config) {
        // TODO Auto-generated method stub
        
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
