package pl.edu.agh.ki.grieg.playback;

import java.io.IOException;

import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.Controllable;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;

/**
 * Class representing a concrete playback functionality, consisting of pair
 * (source, sink).
 * 
 * @author los
 */
class TrackPlayback implements Controllable {

    /** Output to send audio data to */
    private AudioOutput output;

    /** Audio data source */
    private SampleEnumerator source;

    /**
     * Creates a playback object using supplied output and sourc.
     */
    public TrackPlayback(AudioOutput output, SampleEnumerator source) {
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

}