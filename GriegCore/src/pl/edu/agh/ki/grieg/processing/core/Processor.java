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
import pl.edu.agh.ki.grieg.processing.util.PropertiesHelper;
import pl.edu.agh.ki.grieg.util.Key;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.PropertyMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Class representing complete analysis process for one audio source. It is
 * created by the {@link Analyzer} and has public methods corresponding to
 * phases of analysis:
 * <ol>
 * <li>{@link #openFile()}
 * <li>{@link #preAnalyze()}
 * <li>{@link #analyze()}
 * </ol>
 * that should be invoked in this particular order in order to carry out a full
 * audio analysis.
 * 
 * @author los
 */
public class Processor {

    private static final Logger logger = LoggerFactory
            .getLogger(Processor.class);

    /** Audio file to be processed */
    private final File file;

    /** File loader used to load the file */
    private final FileLoader loader;

    /** Used to create processing pipeline */
    private final PipelineAssembler assembler;

    /** Configuration obtained from some external source */
    private final Properties config;

    /** Audio properties */
    private final Properties results;

    /** Collection of listeners */
    private final List<ProcessingListener> listeners = Lists.newArrayList();

    /** Audio file being processed */
    private AudioFile audioFile;

    public Processor(File file, FileLoader loader, PipelineAssembler assembler,
            Properties config) {
        this.file = file;
        this.loader = loader;
        this.assembler = assembler;
        this.config = config;
        this.results = new PropertyMap();
    }

    /**
     * Determines appropriate parser for the file it is supposed to process
     * using its {@link FileLoader}. Informs all the registered listeners about
     * the result of this action, calling
     * {@link ProcessingListener#fileOpened(AudioFile)} on succes.
     * 
     * @throws AudioException
     *             If there is a problem with interpreting the audio file
     * @throws IOException
     *             If an IO error occured
     */
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

    /**
     * Carries out a preliminary analysis, gathering simple properties of the
     * audio file, without using the processing tree. Informs all the registered
     * listeners and gathers information about which file properties are needed.
     * Having finished the processing it informs the listeners about its'
     * result, using {@link ProcessingListener#afterPreAnalysis(Properties)}.
     * 
     * @throws AudioException
     *             If there is a problem with the audio file
     * @throws IOException
     *             If an IO error occured
     */
    public void preAnalyze() throws AudioException, IOException {
        try {
            Set<Key<?>> keys = Sets.newHashSet();
            Properties config = new PropertyMap();
            logger.info("Beginning pre-analysis");
            signalBeforePreAnalysis(keys, config);
            logger.debug("Extracting audio properties from the file");
            Properties info = audioFile.computeAll(keys);
            logger.debug("Properties have been computed");
            logComputedProperties(info);
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

    /**
     * Logs all the (key, value) pairs in the specified property container.
     * 
     * @param info
     *            Properties to log
     */
    private void logComputedProperties(Properties info) {
        logger.debug("Computed properties:\n{}", PropertiesHelper.dump(info));
    }

    /**
     * Carries out the actual, full analysis, using pipeline created by the
     * {@link PipelineAssembler} instance used by thie {@code Processor}.
     * Registered listeners are informed through the
     * {@link ProcessingListener#beforeAnalysis(Pipeline)} method call.
     * Listeners may at this point modify/connect to the pipeline to receive
     * output.
     * 
     * @throws AudioException
     *             If there is a problem with decoding the audio file
     * @throws IOException
     *             If an IO error occured
     */
    public void analyze() throws AudioException, IOException {
        try {
            logger.info("Beginning main analysis");
            Pipeline<float[][]> pipeline = Pipeline.make(float[][].class);
            logger.debug("Assembling processing tree");
            assembler.build(pipeline, config, results);
            logger.debug("Creating an audio source");
            SampleEnumerator source = audioFile.openSource();
            source.connect(pipeline);
            logger.debug("Audio source created");
            signalBeforeAnalysis(pipeline);
            logger.debug("Activating audio source");
            source.start();
            logger.info("Main analysis has been completed");
            source.disconnect(pipeline);
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

    /**
     * @return Configuration properties
     */
    public Properties getConfig() {
        return config;
    }

    /**
     * @return Information about the audio file gathered while processing
     */
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
    private void signalFileOpened(AudioFile audioFile) {
        logger.trace("Notifying {} listener(s) about opening the file",
                listeners.size());
        for (ProcessingListener listener : listeners) {
            listener.fileOpened(audioFile, config);
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
    private void signalBeforePreAnalysis(Set<Key<?>> desired, Properties config) {
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
    private void signalAfterPreAnalysis(Properties info) {
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
    private void signalBeforeAnalysis(Pipeline<float[][]> pipeline) {
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
    private void signalAfterAnalysis() {
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
    private void signalFailure(Throwable e) {
        logger.trace("Notifying {} listener(s) about the error",
                listeners.size());
        for (ProcessingListener listener : listeners) {
            listener.failed(e);
        }
        logger.trace("All the listeners have been notified");
    }

}
