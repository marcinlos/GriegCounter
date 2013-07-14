package pl.edu.agh.ki.grieg.io;

import java.io.IOException;

import pl.edu.agh.ki.grieg.data.Format2;
import pl.edu.agh.ki.grieg.utils.iteratee.Enumerator;

/**
 * Raw audio data producer. Pushes chunks of samples to the attached iteratees.
 * 
 * @author los
 */
public interface SampleEnumerator extends Enumerator<float[][]> {

    /**
     * @return Format2 of the pushed data
     */
    Format2 getFormat();

    /**
     * Begins pushing data to attached iteratees.
     * 
     * @throws IOException
     * @throws AudioException
     */
    void start() throws AudioException, IOException;

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
