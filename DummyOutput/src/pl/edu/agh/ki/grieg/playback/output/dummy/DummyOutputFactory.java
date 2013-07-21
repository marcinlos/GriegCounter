package pl.edu.agh.ki.grieg.playback.output.dummy;

import java.io.IOException;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.playback.output.AudioOutput;
import pl.edu.agh.ki.grieg.playback.spi.OutputFactory;
import pl.edu.agh.ki.grieg.utils.iteratee.State;

/**
 * Trivial dummy factory, returning implementation that just logs the
 * operations.
 * 
 * @author los
 */
public class DummyOutputFactory implements OutputFactory {

    /** One shared instance of {@link AudioOutput} */
    private static final AudioOutput output = new AudioOutput() {

        @Override
        public State step(float[][] item) {
            System.out.println("[Dummy audio output] received "
                    + item[0].length + " samples");
            return State.Cont;
        }

        @Override
        public void finished() {
            System.out.println("[Dummy audio output] Finished");
        }

        @Override
        public void failed(Throwable e) {
            System.err.println("[Dummy audio output] exception: ");
            e.printStackTrace(System.err);
        }

        @Override
        public void close() throws IOException {
            System.out.println("[Dummy audio output] close");
        }

        @Override
        public void write(float[][] data, int offset, int length) {
            System.out.println("[Dummy audio output] writing " + length
                    + " samples");
        }

        @Override
        public void write(float[][] data) {
            write(data, 0, data[0].length);
        }

        @Override
        public void start() {
            System.out.println("[Dummy audio output] start");
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public AudioOutput newOutput(SoundFormat format) {
        return output;
    }

}
