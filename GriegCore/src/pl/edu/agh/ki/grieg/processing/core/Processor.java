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
import pl.edu.agh.ki.grieg.util.Key;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.PropertyMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Processor {

    private static final Logger logger = LoggerFactory
            .getLogger(Processor.class);

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

    public void openFile() throws AudioException, IOException {
        try {
            audioFile = loader.loadFile(file);
            logger.info("Opened file {}", audioFile);
            logger.debug("Using parser {}", audioFile.getParser());
            signalFileOpened(audioFile);
        } catch (AudioException e) {
            signalFailure(e);
            throw e;
        } catch (IOException e) {
            signalFailure(e);
            throw e;
        }

    }

    public void preAnalyze() throws AudioException, IOException {
        try {
            Set<Key<?>> keys = Sets.newHashSet();
            Properties config = new PropertyMap();
            logger.info("Beginning pre-analysis");
            signalBeforePreAnalysis(keys, config);
            logger.debug("Extracting audio properties from the file");
            Properties info = audioFile.computeAll(keys);
            results.addAll(info);
            signalAfterPreAnalysis(results);
            logger.info("Pre-analysis has been completed");
        } catch (AudioException e) {
            signalFailure(e);
            throw e;
        } catch (IOException e) {
            signalFailure(e);
            throw e;
        }
    }

    public void analyze() throws AudioException, IOException {
        try {
            logger.info("Beginning main analysis");
            tree = Pipeline.make(float[][].class);
            logger.debug("Assembling processing tree");
            assembler.build(tree, config, results);
            logger.debug("Creating an audio source");
            SampleEnumerator source = audioFile.openSource();
            source.connect(tree);
            logger.debug("Audio source created");
            signalBeforeAnalysis(tree);
            logger.debug("Activating audio source");
            source.start();
            logger.info("Main analysis has been completed");
            signalAfterAnalysis();
        } catch (AudioException e) {
            signalFailure(e);
            throw e;
        } catch (IOException e) {
            signalFailure(e);
            throw e;
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
        logger.debug("Adding listener: {}", listener);
        listeners.add(listener);
    }

    /**
     * Registers a sequence of listeners
     * 
     * @param iterable
     *            Sequence of listeners
     */
    public void addAll(Iterable<? extends ProcessingListener> iterable) {
        logger.debug("Adding multiple listeners");
        for (ProcessingListener listener : iterable) {
            addListener(listener);
        }
        logger.debug("All the listeners have been added");
    }

    /**
     * Removes a listener from the list
     * 
     * @param listener
     *            Listener to be disconnected
     */
    public void removeListener(ProcessingListener listener) {
        logger.debug("Removing listener: {}", listener);
        listeners.remove(listener);
    }

    /**
     * Notifies all the listeners that an audio file has been opened.
     * 
     * @param audioFile
     */
    public void signalFileOpened(AudioFile audioFile) {
        logger.trace("Notifying {} listener(s) about opening the file",
                listeners.size());
        for (ProcessingListener listener : listeners) {
            listener.fileOpened(audioFile);
        }
        logger.trace("All the listeners have been notified");
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
        logger.trace("Notifying {} listener(s) about pre-analysis",
                listeners.size());
        for (ProcessingListener listener : listeners) {
            listener.beforePreAnalysis(desired, config);
        }
        logger.trace("All the listeners have been notified");
    }

    /**
     * Notifies all the listeners pre-analysis has been completed.
     * 
     * @param info
     *            Audio properties collected during pre-analysis
     */
    public void signalAfterPreAnalysis(Properties info) {
        logger.trace("Notifying {} listener(s) about the end of pre-analysis",
                listeners.size());
        for (ProcessingListener listener : listeners) {
            listener.afterPreAnalysis(info);
        }
        logger.trace("All the listeners have been notified");
    }

    /**
     * Notifies all the listeners that main analysis is about to begin.
     * 
     * @param pipeline
     *            Processing pipeline
     */
    public void signalBeforeAnalysis(Pipeline<float[][]> pipeline) {
        logger.trace("Notifying {} listener(s) about the beginning of "
                + "analysis", listeners.size());
        for (ProcessingListener listener : listeners) {
            listener.beforeAnalysis(pipeline);
        }
        logger.trace("All the listeners have been notified");
    }

    /**
     * Notifies all the listeners the main analysis has been completed.
     */
    public void signalAfterAnalysis() {
        logger.trace("Notifying {} listener(s) about the end of analysis",
                listeners.size());
        for (ProcessingListener listener : listeners) {
            listener.afterAnalysis();
        }
        logger.trace("All the listeners have been notified");
    }

    /**
     * Notifies all the listeners about the processing failure.
     * 
     * @param e
     *            Exception that caused the failure
     */
    public void signalFailure(Throwable e) {
        logger.trace("Notifying {} listener(s) about the error",
                listeners.size());
        for (ProcessingListener listener : listeners) {
            listener.failed(e);
        }
        logger.trace("All the listeners have been notified");
    }

}
