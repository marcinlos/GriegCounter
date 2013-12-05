package pl.edu.agh.ki.grieg.playback.output;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import pl.edu.agh.ki.grieg.data.SoundFormat;

/**
 * Abstract base class for outputs that perform conversion of input floating
 * point PCM data to byte array containing 16-bit signed PCM data prior to
 * sending it to the acutal output.
 * 
 * @author los
 */
public abstract class Abstract16BitSignedPCMAudioOutput extends
        AbstractAudioOutput {

    public Abstract16BitSignedPCMAudioOutput(SoundFormat format) {
        super(format);
    }
    
    /**
     * Converts a float sample to 16-bit signed one.
     * 
     * @param a
     *            Float representing a sample
     * @return 16-bit signed integer representing the same sample
     */
    private static final short toSignedShort(float a) {
        return (short) (a * Short.MAX_VALUE);
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
        int channels = format().getChannels();
        byte[] buffer = new byte[length * channels * 2];
        ShortBuffer shorts = ByteBuffer.wrap(buffer)
                .order(ByteOrder.BIG_ENDIAN).asShortBuffer();
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < channels; ++j) {
                shorts.put(toSignedShort(data[j][offset + i]));
            }
        }
        return buffer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(float[][] data, int offset, int length) {
        byte[] buffer = toSigned16bit(data, offset, length);
        writeBytes(buffer);
    }

    /**
     * Sends the byte data to the real output.
     * 
     * @param data
     *            16-bit signed PCM data
     */
    protected abstract void writeBytes(byte[] data);

}
