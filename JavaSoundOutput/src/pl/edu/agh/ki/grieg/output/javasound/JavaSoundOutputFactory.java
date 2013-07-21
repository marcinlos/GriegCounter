package pl.edu.agh.ki.grieg.output.javasound;


import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.output.spi.OutputFactory;
import pl.edu.agh.ki.grieg.playback.PlaybackException;
import pl.edu.agh.ki.grieg.playback.output.AudioOutput;

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
