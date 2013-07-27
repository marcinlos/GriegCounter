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
        checkNotNull(output, "Null audio output");
        checkNotNull(source, "Null audio source");
        this.output = output;
        this.source = source;
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
        output.start();
        source.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        source.pause();
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
    public void stop() {
        source.stop();
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