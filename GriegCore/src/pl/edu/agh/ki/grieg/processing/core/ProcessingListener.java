package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.meta.ExtractionContext;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.Properties;

/**
 * Interface for receiving notifications about important events of the
 * processing lifecycle.
 * 
 * @author los
 */
public interface ProcessingListener {

    /**
     * Invoked when processing of the new file begins (before actually analysing
     * any data etc).
     * 
     * @param file
     *            Audio file whose processing begins
     * @param config
     *            Read-only configuration
     */
    void fileOpened(AudioFile file, Properties config);

    /**
     * Invoked when the process of extracting metadata is about to begin. By
     * manipulating contents of the {@code desired} set, component can provide
     * clues as to what data is needed. There is no guarantee that all the
     * pieces of information are actually collected. Second parameter allows
     * components provide additional hints and configuration parameters for the
     * process.
     * 
     * @param ctx
     *            Extraction context, allowing to specify desired features
     */
    void beforePreAnalysis(ExtractionContext ctx);

    /**
     * Invoked when metadata for the audio file has been gathered during
     * processing.
     * 
     * @param results
     *            Information abouth the file
     * @param config
     *            Configuration properties for the rest of the process
     */
    void afterPreAnalysis(Properties results);

    /**
     * Invoked before the actual sound data processing takes place. In this
     * method components may connect to tree leaf they see fit.
     * 
     * @param pipeline
     *            Structure defining the data flow
     */
    void beforeAnalysis(Pipeline<float[][]> pipeline);

    /**
     * Invoked after the main phase of sound analysis has been completed.
     */
    void afterAnalysis();

    /**
     * Invoked when the processing fails (i.e. encounters fatal error) and can
     * no longer proceed.
     * 
     * @param e
     *            Exception that caused the failure
     */
    void failed(Throwable e);

}
