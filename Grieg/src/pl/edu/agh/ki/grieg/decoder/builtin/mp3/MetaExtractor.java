package pl.edu.agh.ki.grieg.decoder.builtin.mp3;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;
import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.meta.AudioFeatures;
import pl.edu.agh.ki.grieg.meta.ExtractionContext;

import com.google.common.io.Closeables;

class MetaExtractor {

    protected final ExtractionContext ctx;

    public MetaExtractor(ExtractionContext context) {
        this.ctx = context;
    }

    public void extract() throws DecodeException, IOException {
        if (ctx.shouldCompute(AudioFeatures.SAMPLES)) {
            determineLength();
        }
    }

    public void determineLength() throws IOException, DecodeException {
        InputStream stream = null;
        long count = 0;
        try {
            stream = new BufferedInputStream(new FileInputStream(ctx.getFile()));
            FrameReader reader = new FrameReader(stream);
            boolean first = true;
            for (Header header : reader) {
                SampleBuffer samples = reader.readSamples(header);
                int bufferSize = samples.getBufferLength();
                int channels = samples.getChannelCount();
                count += bufferSize / channels;
                if (first) {
                    ctx.setFeature(AudioFeatures.FORMAT, extractFormat(samples));
                }
                first = false;
                reader.closeFrame();
            }
            ctx.setFeature(AudioFeatures.SAMPLES, count);
        } catch (DecoderException e) {
            throw new DecodeException(e);
        } finally {
            Closeables.close(stream, true);
        }
    }

    public static SoundFormat extractFormat(SampleBuffer samples) {
        int freq = samples.getSampleFrequency();
        int channels = samples.getChannelCount();
        return new SoundFormat(freq, channels);
    }

}
