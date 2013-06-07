package pl.edu.agh.ki.grieg.io;

import pl.edu.agh.ki.grieg.data.Format;
import pl.edu.agh.ki.grieg.utils.Enumerator;

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

}
