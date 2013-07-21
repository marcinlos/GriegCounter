package pl.edu.agh.ki.grieg.playback.spi;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.playback.PlaybackException;
import pl.edu.agh.ki.grieg.playback.output.AudioOutput;

/**
 * Interface of a factory producing {@link AudioOutput}s for specified formats.
 * 
 * @author los
 */
public interface OutputFactory {

    /**
     * Creates new {@link AudioOutput} for specified format
     * 
     * @param format
     *            Format of the input data
     * @return {@link AudioOutput} suitable for playing this data
     * @throws PlaybackException
     *             If the output cannot be created
     */
    AudioOutput newOutput(SoundFormat format) throws PlaybackException;

}
