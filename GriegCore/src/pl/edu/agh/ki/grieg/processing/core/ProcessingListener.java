package pl.edu.agh.ki.grieg.processing.core;

import java.util.Set;

import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.meta.MetaInfo;
import pl.edu.agh.ki.grieg.meta.MetaKey;
import pl.edu.agh.ki.grieg.processing.tree.FlowTree;

public interface ProcessingListener {

    /**
     * Invoked when processing of new audio source is about to start
     * 
     * @param context
     *            Processing context
     */
    //void processingStarted(Context context);

    /**
     * Invoked when processing of the new file begins (before actually analysing
     * any data etc)
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
    void readingMetaInfo(Set<MetaKey<?>> desired);

    /**
     * Invoked when metadata for the audio file has been gathered during
     * processing.
     * 
     * @param info
     *            Information abouth the file
     */
    void gatheredMetainfo(MetaInfo info);

    void processingStarted(FlowTree<float[][]> flow);
    
    /**
     * Invoked when the processing fails (i.e. encounters fatal error) and can
     * no longer proceed.
     * 
     * @param e
     *            Exception that caused the failure
     */
    void failed(Exception e);

}
