package pl.edu.agh.ki.grieg.processing.core;

import java.io.File;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.utils.Properties;
import pl.edu.agh.ki.grieg.utils.PropertyMap;

public class Analyzer {
    
    /** File loader used to open audio files */
    private final FileLoader loader;
    
    /** Collection of listeners */
    private final ProcessingListenerList listeners = new ProcessingListenerList();

    private final Properties config = new PropertyMap();
    
    private Processor currentProcessor;
    
    public Analyzer(FileLoader loader) {
        this.loader = loader;
        
        
        
    }
    
    public Processor newProcessing(File file) {
        Processor processor = new Processor(loader);
        processor.addAll(listeners);
        processor.openFile(file);
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
