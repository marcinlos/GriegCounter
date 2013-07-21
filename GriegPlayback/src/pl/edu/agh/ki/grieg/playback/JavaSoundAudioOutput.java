package pl.edu.agh.ki.grieg.playback;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.playback.output.Abstract16BitSignedPCMAudioOutput;

/**
 * Represents a format-specific audio output. Can be used e.g. to play a single
 * track. Uses JavaSound, so it's not suitable for Android.
 * 
 * @author los
 */
public class JavaSoundAudioOutput extends Abstract16BitSignedPCMAudioOutput {

    /** Output buffer */
    private SourceDataLine line;

    /**
     * Creates new {@code AudioOutput} compatible with the specified format.
     * 
     * @param format
     *            Format of the output
     * @throws PlaybackException
     *             If the output line could not be created
     */
    public JavaSoundAudioOutput(SoundFormat format) throws PlaybackException {
        super(format);
        try {
            AudioFormat fmt = toJavaFormat(format);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, fmt);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(fmt);
        } catch (LineUnavailableException e) {
            throw new PlaybackException(e);
        }
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
     * {@inheritDoc}
     */
    public void start() {
        line.start();
    }

    /**
     * Closes the output, having flushed the buffer.
     */
    @Override
    public void close() {
        line.drain();
        line.stop();
        line.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeBytes(byte[] data) {
        line.write(data, 0, data.length);
    }

}
