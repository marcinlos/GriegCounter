package pl.edu.agh.ki.grieg.io;

import pl.edu.agh.ki.grieg.data.Format;
import pl.edu.agh.ki.grieg.utils.iteratee.Enumerator;

/**
 * Raw audio data producer. Pushes chunks of samples to the attached iteratees.
 * 
 * @author los
 */
public interface SampleEnumerator extends Enumerator<float[][]> {

    /**
     * @return Format of the pushed data
     */
    Format getFormat();
    
    /**
     * Begins pushing data to attached iteratees.
     */
    void start();
    
    /**
     * Pauses data processing with the possibiliity to resume it later.
     */
    void pause();
    
    /**
     * Resumes previously paused processing.
     */
    void resume();

    /**
     * Definitely stops processing, informs attached iteratees.
     */
    void stop();

}
