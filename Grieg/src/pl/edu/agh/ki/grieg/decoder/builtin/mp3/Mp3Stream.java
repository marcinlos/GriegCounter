package pl.edu.agh.ki.grieg.decoder.builtin.mp3;

import java.io.IOException;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;
import pl.edu.agh.ki.grieg.data.Format;
import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.util.PCM;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.meta.SimpleTagContainer;

public class Mp3Stream implements AudioStream {

    private short[] sampleBuffer;
    private short sampleOffset;

    private Bitstream bitstream;
    private SourceDetails details;

    public Mp3Stream(Bitstream stream) throws DecodeException, IOException {
        this.bitstream = stream;
        decodeNext();
    }

    private boolean hasSomeBuffered() {
        return sampleBuffer != null && sampleOffset < sampleBuffer.length - 1;
    }

    private boolean decodeNext() throws DecodeException, IOException {
        try {
            Header frame = bitstream.readFrame();
            if (frame != null) {
                SampleBuffer samples = (SampleBuffer) Mp3Parser.decoder
                        .decodeFrame(frame, bitstream);
                if (details == null) {
                    details = extractDetails(samples);
                }
                sampleBuffer = samples.getBuffer();
                sampleOffset = 0;
                bitstream.closeFrame();
                return true;
            }
        } catch (DecoderException e) {
            throw new DecodeException(e);
        } catch (BitstreamException e) {
            throw new IOException(e);
        }
        return false;
    }

    private SourceDetails extractDetails(SampleBuffer samples) {
        int freq = samples.getSampleFrequency();
        int channels = samples.getChannelCount();
        Format format = new Format(channels, freq);
        SimpleTagContainer tags = new SimpleTagContainer();
        return new SourceDetails(null, -1, -1, format, tags);
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
            for (int j = 0; j < channels; ++j) {
                buffer[j][offset + i] = PCM
                        .fromShort(sampleBuffer[sampleOffset++]);
            }
        }
        return n;
    }

    public SourceDetails getDetails() {
        return details;
    }

    @Override
    public Format getFormat() {
        return details.getFormat();
    }

}
