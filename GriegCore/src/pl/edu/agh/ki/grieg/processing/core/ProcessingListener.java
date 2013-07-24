package pl.edu.agh.ki.grieg.processing.core;

import java.util.Set;

import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.processing.tree.ProcessingTree;
import pl.edu.agh.ki.grieg.utils.Key;
import pl.edu.agh.ki.grieg.utils.Properties;

public interface ProcessingListener {

    /**
     * Invoked when processing of the new file begins (before actually analysing
     * any data etc).
     * 
     * @param file
     *            Audio file whose processing begins
     */
    void fileOpened(AudioFile file);

    /**
     * Invoked when the process of extracting metadata is about to begin. By
     * manipulating contents of the {@code desired} set, component can provide
     * clues as to what data is needed. There is no guarantee that all the
     * pieces of information are actually collected.
     * 
     * @param desired
     *            Set of metadata that should be gathered during the processing
     */
    void readingMetaInfo(Set<Key<?>> desired);

    /**
     * Invoked when metadata for the audio file has been gathered during
     * processing.
     * 
     * @param info
     *            Information abouth the file
     */
    void gatheredMetainfo(Properties info);

    /**
     * Invoked before the actual sound data processing takes place. In this
     * method components may connect to tree leaf they see fit.
     * 
     * @param tree
     *            Structure defining the data flow
     */
    void processingStarted(ProcessingTree<float[][]> tree);

    /**
     * Invoked when the processing fails (i.e. encounters fatal error) and can
     * no longer proceed.
     * 
     * @param e
     *            Exception that caused the failure
     */
    void failed(Exception e);

}
