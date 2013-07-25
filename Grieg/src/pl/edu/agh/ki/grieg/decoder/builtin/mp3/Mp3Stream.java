package pl.edu.agh.ki.grieg.decoder.builtin.mp3;

import java.io.IOException;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;
import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.util.PCM;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioStream;

class Mp3Stream implements AudioStream {

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
                format = MetaExtractor.extractFormat(samples);
            }
            sampleBuffer = samples.getBuffer();
            sampleOffset = 0;
            bitstream.closeFrame();
            return true;
        } else {
            done = true;
            return false;
        }
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
        while (written < buffer[0].length) {
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
