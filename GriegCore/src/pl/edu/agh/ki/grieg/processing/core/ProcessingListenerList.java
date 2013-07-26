package pl.edu.agh.ki.grieg.processing.core;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.utils.Key;
import pl.edu.agh.ki.grieg.utils.Properties;

import com.google.common.collect.Lists;

/**
 * Thread-safe list of {@link ProcessingListener}s. Implements
 * {@link ProcessingListener} interface for convenience, forwards all the
 * invocations to all the stored listeners.
 * 
 * @author los
 */
class ProcessingListenerList implements ProcessingListener,
        Iterable<ProcessingListener> {

    /** Actual list of listeners */
    private final List<ProcessingListener> listeners = Lists
            .newCopyOnWriteArrayList();

    /**
     * Adds listener to the list
     */
    public void add(ProcessingListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes listener from the list
     */
    public void remove(ProcessingListener listener) {
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fileOpened(AudioFile file) {
        for (ProcessingListener listener : listeners) {
            listener.fileOpened(file);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readingMetaInfo(Set<Key<?>> desired, Properties config) {
        for (ProcessingListener listener : listeners) {
            listener.readingMetaInfo(desired, config);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gatheredMetainfo(Properties info) {
        for (ProcessingListener listener : listeners) {
            listener.gatheredMetainfo(info);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processingStarted(Pipeline<float[][]> flow) {
        for (ProcessingListener listener : listeners) {
            listener.processingStarted(flow);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failed(Exception e) {
        for (ProcessingListener listener : listeners) {
            listener.failed(e);
        }
    }

    @Override
    public Iterator<ProcessingListener> iterator() {
        return listeners.iterator();
    }

}
