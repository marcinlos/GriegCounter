package pl.edu.agh.ki.grieg.io;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.util.iteratee.Enumerator;

/**
 * Raw audio data producer. Pushes chunks of samples to the attached iteratees.
 * 
 * @author los
 */
public interface SampleEnumerator extends Enumerator<float[][]>, Controllable {

    /**
     * @return Format of the pushed data
     */
    SoundFormat getFormat();

}
