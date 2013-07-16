package pl.edu.agh.ki.grieg.playback;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.decoder.util.PCM;
import pl.edu.agh.ki.grieg.utils.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.utils.iteratee.State;

/**
 * Represents a format-specific audio output. Can be used e.g. to play a single
 * track. Uses JavaSound, so it's not suitable for Android.
 * 
 * @author los
 */
public class AudioOutput implements Closeable, Iteratee<float[][]> {

    /** Output buffer */
    private SourceDataLine line;

    /** Format of the supported data */
    private SoundFormat format;

    /**
     * Creates new {@code AudioOutput} compatible with the specified format.
     * 
     * @param format
     *            Format of the output
     * @throws LineUnavailableException
     *             If the output line could not be created
     */
    public AudioOutput(SoundFormat format) throws LineUnavailableException {
        this.format = format;
        AudioFormat audioFmt = toJavaFormat(format);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFmt);
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(audioFmt);
    }

    /**
     * Converts the {@link SoundFormat} object into JavaSound
     * {@link AudioFormat} object.
     * 
     * @param format
     *            Grieg's native format
     * @return JavaSound format
     */
    private AudioFormat toJavaFormat(SoundFormat format) {
        int rate = format.getSampleRate();
        int channels = format.getChannels();
        AudioFormat audioFmt = new AudioFormat(rate, 16, channels, true, true);
        return audioFmt;
    }

    /**
     * Starts playback using available data. Does not block.
     */
    public void start() {
        line.start();
    }

    /**
     * Sends PCM data to the output represented by this object.
     * 
     * @param data
     *            Floating-point PCM-encoded audio data
     */
    public void write(float[][] data) {
        write(data, 0, data[0].length);
    }

    /**
     * Sends PCM data to the output represented by this object.
     * 
     * @param data
     *            Buffer containing floating-point PCM-encoded audio data
     * @param offset
     *            Offset of the beginning of the data that should be pushed to
     *            the output
     * @param length
     *            Amount of samples to be pushed
     */
    public void write(float[][] data, int offset, int length) {
        byte[] buffer = toSigned16bit(data, offset, length);
        line.write(buffer, 0, buffer.length);
    }

    /**
     * Converts floating-point PCM data to 16-bit signed data.
     * 
     * @param data
     *            Input buffer
     * @param offset
     *            Offset at which the data to be processed begins
     * @param length
     *            Amount of data to be processed
     * @return Byte buffer containing converted PCM data
     */
    private byte[] toSigned16bit(float[][] data, int offset, int length) {
        int channels = format.getChannels();
        byte[] buffer = new byte[length * channels * 2];
        ShortBuffer shorts = ByteBuffer.wrap(buffer)
                .order(ByteOrder.BIG_ENDIAN).asShortBuffer();
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < channels; ++j) {
                shorts.put(PCM.toSignedShort(data[j][offset + i]));
            }
        }
        return buffer;
    }

    /**
     * Closes the output, having flushed the buffer.
     */
    public void close() {
        line.drain();
        line.stop();
        line.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State step(float[][] item) {
        write(item);
        return State.Cont;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finished() {
        close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failed(Throwable e) {
        close();
    }

}
