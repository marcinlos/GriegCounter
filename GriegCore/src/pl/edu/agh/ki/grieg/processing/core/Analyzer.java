package pl.edu.agh.ki.grieg.processing.core;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.util.Properties;

import com.google.common.collect.Lists;

public class Analyzer {
    
    private static final Logger logger = LoggerFactory.getLogger(Analyzer.class);

    /** File loader used to open audio files */
    private final FileLoader loader;

    private final PipelineAssembler assembler;

    private final Properties properties;

    /** Collection of listeners */
    private final List<ProcessingListener> listeners = Lists.newArrayList();

    private Processor currentProcessor;

    public Analyzer(AnalyzerConfig config) {
        this.loader = config.getFileLoader();
        this.assembler = config.getPipelineFactory();
        this.properties = config.getProperties();
    }
    
    public Processor newProcessing(File file) {
        Processor processor = new Processor(file, loader, assembler, properties);
        processor.addAll(listeners);
        currentProcessor = processor;
        return processor;
    }

    public Processor getCurrentProcessor() {
        return currentProcessor;
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
