package pl.edu.agh.ki.grieg.processing.core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.utils.Key;
import pl.edu.agh.ki.grieg.utils.Properties;
import pl.edu.agh.ki.grieg.utils.PropertyMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Processor {

    private static final Logger logger = LoggerFactory.getLogger(Processor.class);
    
    private final File file;
    
    private final FileLoader loader;

    private final PipelineAssembler assembler;

    private final Properties config;

    private final Properties results;

    /** Collection of listeners */
    private final List<ProcessingListener> listeners = Lists.newArrayList();

    private AudioFile audioFile;

    private Pipeline<float[][]> tree;

    
    public Processor(File file, FileLoader loader, PipelineAssembler assembler,
            Properties config) {
        this.file = file;
        this.loader = loader;
        this.assembler = assembler;
        this.config = config;
        this.results = new PropertyMap();
    }
    

    public void openFile() {
        try {
            audioFile = loader.loadFile(file);
            logger.info("Opened file {}", audioFile);
            signalFileOpened(audioFile);
        } catch (AudioException e) {
            signalFailure(e);
        } catch (IOException e) {
            signalFailure(e);
        }

    }

    public void preAnalyze() {
        try {
            Set<Key<?>> keys = Sets.newHashSet();
            Properties config = new PropertyMap();
            signalBeforePreAnalysis(keys, config);
            Properties info = audioFile.computeAll(keys);
            signalAfterPreAnalysis(info);
        } catch (AudioException e) {
            signalFailure(e);
        } catch (IOException e) {
            signalFailure(e);
        }
    }

    public void analyze() {
        try {
            tree = Pipeline.make(float[][].class);
            assembler.build(tree, config, results);
            SampleEnumerator source = audioFile.openSource();
            source.connect(tree);
            signalBeforeAnalysis(tree);
            source.start();
            signalAfterAnalysis();
        } catch (AudioException e) {
            signalFailure(e);
        } catch (IOException e) {
            signalFailure(e);
        }
    }

    /**
     * @return Audio audioFile being processed
     */
    public AudioFile getFile() {
        return audioFile;
    }

    public Properties getConfig() {
        return config;
    }

    public Properties getResults() {
        return results;
    }

    /**
     * Registers new processing listener
     * 
     * @param listener
     *            Listener to be added to the list
     */
    public void addListener(ProcessingListener listener) {
        listeners.add(listener);
    }

    /**
     * Registers a sequence of listeners
     * 
     * @param iterable
     *            Sequence of listeners
     */
    public void addAll(Iterable<? extends ProcessingListener> iterable) {
        for (ProcessingListener listener : iterable) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a listener from the list
     * 
     * @param listener
     *            Listener to be disconnected
     */
    public void removeListener(ProcessingListener listener) {
        listeners.remove(listener);
    }

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
     * Notifies all the listeners that an audio file has been opened.
     * 
     * @param audioFile
     */
    public void signalFileOpened(AudioFile audioFile) {
        for (ProcessingListener listener : listeners) {
            listener.fileOpened(audioFile);
        }
    }

    /**
     * Notifies all the listeners pre-analysis is about to begin.
     * 
     * @param desired
     *            Set of audio properties to be collected during pre-analysis
     * @param config
     *            Configuration properties
     */
    public void signalBeforePreAnalysis(Set<Key<?>> desired, Properties config) {
        for (ProcessingListener listener : listeners) {
            listener.beforePreAnalysis(desired, config);
        }
    }

    /**
     * Notifies all the listeners pre-analysis has been completed.
     * 
     * @param info
     *            Audio properties collected during pre-analysis
     */
    public void signalAfterPreAnalysis(Properties info) {
        for (ProcessingListener listener : listeners) {
            listener.afterPreAnalysis(info);
        }
    }

    /**
     * Notifies all the listeners that main analysis is about to begin.
     * 
     * @param pipeline
     *            Processing pipeline
     */
    public void signalBeforeAnalysis(Pipeline<float[][]> pipeline) {
        for (ProcessingListener listener : listeners) {
            listener.beforeAnalysis(pipeline);
        }
    }

    /**
     * Notifies all the listeners the main analysis has been completed.
     */
    public void signalAfterAnalysis() {
        for (ProcessingListener listener : listeners) {
            listener.afterAnalysis();
        }
    }

    /**
     * Notifies all the listeners about the processing failure.
     * 
     * @param e
     *            Exception that caused the failure
     */
    public void signalFailure(Throwable e) {
        for (ProcessingListener listener : listeners) {
            listener.failed(e);
        }
    }

}
