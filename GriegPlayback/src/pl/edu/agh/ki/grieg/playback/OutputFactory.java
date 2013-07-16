package pl.edu.agh.ki.grieg.playback;

import java.io.IOException;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;

public class OutputFactory {
    
    public AudioOutput newOutput(SoundFormat format) throws PlaybackException {
        return new AudioOutput(format);
    }

    public AudioOutput boundTo(SampleEnumerator source)
            throws AudioException, IOException {
        AudioOutput output = newOutput(source.getFormat());
        source.connect(output);
        output.start();
        return output;
    }
    
    
}
