package pl.edu.agh.ki.grieg.playback;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.Controllable;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.playback.output.AudioOutput;
import pl.edu.agh.ki.grieg.util.iteratee.Enumerator;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;

/**
 * Class representing a concrete playback functionality, consisting of pair
 * (source, sink).
 * 
 * @author los
 */
class TrackPlayback implements Controllable, Enumerator<float[][]> {

    /** Output to send audio data to */
    private final AudioOutput output;

    /** Audio data source */
    private final SampleEnumerator source;

    /**
     * Creates a playback object using supplied output and sourc.
     */
    public TrackPlayback(AudioOutput output, SampleEnumerator source) {
        this.output = checkNotNull(output, "Null audio output");
        this.source = checkNotNull(source, "Null audio source");
    }

    /**
     * @return Audio output
     */
    public AudioOutput getOutput() {
        return output;
    }

    /**
     * @return Audio source
     */
    public SampleEnumerator getSource() {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws AudioException, IOException {
        startOutput();
        source.start();
    }
    
    /**
     * Starts the audio output, without enabling the source. It may be useful
     * if the source is to be enabled externally.
     */
    public void startOutput() {
        output.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean pause() {
        return source.pause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume() {
        source.resume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stop() {
        return source.stop();
        // this will do, output will be closed by the source
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(Iteratee<? super float[][]> consumer) {
        source.connect(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect(Iteratee<? super float[][]> consumer) {
        source.disconnect(consumer);
    }

}