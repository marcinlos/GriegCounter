package pl.edu.agh.ki.grieg.playback;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.playback.output.AudioOutput;
import pl.edu.agh.ki.grieg.playback.spi.OutputFactory;

/**
 * Implementation of {@link OutputFactory} using JavaSound-based
 * {@link AudioOutput}.
 * 
 * @author los
 */
public class JavaSoundOutputFactory implements OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public AudioOutput newOutput(SoundFormat format) throws PlaybackException {
        return new JavaSoundAudioOutput(format);
    }

}
