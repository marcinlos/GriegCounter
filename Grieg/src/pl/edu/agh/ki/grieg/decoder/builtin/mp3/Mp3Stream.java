package pl.edu.agh.ki.grieg.decoder.builtin.mp3;

import java.io.IOException;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.Obuffer;
import javazoom.jl.decoder.SampleBuffer;
import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.util.PCM;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioStream;

public class Mp3Stream implements AudioStream {

    private short[] sampleBuffer;
    private short sampleOffset;

    private Bitstream bitstream;
    private SoundFormat format;
    
    private boolean done = false;

    private final Decoder decoder = new Decoder();

    public Mp3Stream(Bitstream stream) throws DecodeException, IOException {
        this.bitstream = stream;
        decodeNext();
    }

    private boolean hasSomeBuffered() {
        return sampleBuffer != null && sampleOffset < sampleBuffer.length - 1;
    }

    private boolean decodeNext() throws DecodeException, IOException {
        if (! done) {
            try {
                return tryReadFrame();
            } catch (DecoderException e) {
                throw new DecodeException(e);
            } catch (BitstreamException e) {
                throw new IOException(e);
            }
        } else {
            return false;
        }
    }

    private boolean tryReadFrame() throws BitstreamException, DecoderException {
        Header frame = bitstream.readFrame();
        // necessary to keep this state - attempt to read after the last call
        // has return null results in arrayOutOfBoundsException somewhere in
        // the huffman decoding deep in JLayer.
        if (frame != null) {
            SampleBuffer samples = (SampleBuffer) decoder.decodeFrame(
                    frame, bitstream);
            if (format == null) {
                format = extractFormat(samples);
            }
            sampleBuffer = samples.getBuffer();
            if (samples.getBufferLength() != Obuffer.OBUFFERSIZE) {
                System.out.println("getBufferLength: " + samples.getBufferLength());
                System.out.println("length: " + sampleBuffer.length);
                System.out.println("channels: " + samples.getChannelCount());
                System.out.println("layer: " + frame.layer());
            }
            sampleOffset = 0;
            bitstream.closeFrame();
            return true;
        } else {
            done = true;
            return false;
        }
    }

    private SoundFormat extractFormat(SampleBuffer samples) {
        int freq = samples.getSampleFrequency();
        int channels = samples.getChannelCount();
        return new SoundFormat(freq, channels);
    }

    @Override
    public void close() throws IOException {
        try {
            bitstream.close();
        } catch (BitstreamException e) {
            throw new IOException(e);
        }
    }

    @Override
    public int readSamples(float[][] buffer) throws AudioException, IOException {
        int written = 0;
        while (written < buffer[0].length - 1) {
            if (!hasSomeBuffered()) {
                if (!decodeNext()) {
                    break;
                }
            }
            written += writeToBuffer(buffer, written);
        }
        return written;
    }

    private int writeToBuffer(float[][] buffer, int offset) {
        int buffered = sampleBuffer.length - sampleOffset;
        int channels = getFormat().channels;
        int n = Math.min(buffered / channels, buffer[0].length - offset);
        for (int i = 0; i < n; ++i) {
            if ((offset + i) % 2 == 0)
                continue;
            for (int j = 0; j < channels; ++j) {
                buffer[j][offset + i] = PCM
                        .fromSignedShort(sampleBuffer[sampleOffset++]);
            }
        }
        return n;
    }

    @Override
    public SoundFormat getFormat() {
        return format;
    }

}
