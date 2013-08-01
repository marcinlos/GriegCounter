package pl.edu.agh.ki.grieg.processing.core;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.util.Properties;

import com.google.common.collect.Lists;

/**
 * Main class managing the creational aspects of analysis processes. It provides
 * the {@link Processor} with necessary configuration properties and
 * dependencies, as well as the listener list. It is also responsible for the
 * actual instantiation of the {@code Processor} objects.
 * 
 * <p>
 * Analyzer is created by the implementation of {@link Bootstrap}, which is
 * responsible for providing {@code Analyzer} with aforementioned configuration
 * and actual instantiation of the {@link Analyzer}
 * 
 * @author los
 * @see Bootstrap
 */
public class Analyzer {

    private static final Logger logger = LoggerFactory
            .getLogger(Analyzer.class);

    /** File loader used to open audio files */
    private final FileLoader loader;

    /** Assembler used by the created process to build the processing tree */
    private final PipelineAssembler assembler;

    /** Configuration properties */
    private final Properties properties;

    /** Collection of listeners */
    private final List<ProcessingListener> listeners = Lists.newArrayList();

    /** Currently active process */
    private Processor currentProcessor;

    public Analyzer(AnalyzerConfig config) {
        this.loader = config.getFileLoader();
        this.assembler = config.getPipelineFactory();
        this.properties = config.getProperties();
    }

    /**
     * Initiates new analysis of the specified file. Creates the new
     * {@link Processor} object using contained dependencies and configuration.
     * 
     * @param file
     * @return
     */
    public Processor newProcessing(File file) {
        logger.info("Beginning processing new file: {}", file);
        Processor processor = new Processor(file, loader, assembler, properties);
        processor.addAll(listeners);
        currentProcessor = processor;
        return processor;
    }

    /**
     * @return Currently active {@code Processor}
     */
    public Processor getCurrentProcessor() {
        return currentProcessor;
    }

    /**
     * @return Configuration propertiesd
     */
    public Properties getConfig() {
        return properties;
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
     * Removes a listener from the list
     * 
     * @param listener
     *            Listener to be disconnected
     */
    public void removeListener(ProcessingListener listener) {
        listeners.remove(listener);
    }

}
