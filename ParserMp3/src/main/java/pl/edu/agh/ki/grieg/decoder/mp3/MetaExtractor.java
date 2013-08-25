package pl.edu.agh.ki.grieg.decoder.mp3;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;
import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.features.AudioFeatures;
import pl.edu.agh.ki.grieg.features.ExtractionContext;

import com.google.common.io.Closeables;
import com.google.common.io.CountingInputStream;

class MetaExtractor {

    protected final File file;
    
    protected final ExtractionContext ctx;

    public MetaExtractor(File file, ExtractionContext context) {
        this.file = file;
        this.ctx = context;
    }

    public void extract() throws DecodeException, IOException {
        ctx.signalStart();
        try {
            if (ctx.shouldCompute(AudioFeatures.SAMPLES)) {
                determineLength();
            }
            ctx.signalFinish();
        } catch (DecodeException e) {
            ctx.signalFailure(e);
            throw e;
        } catch (IOException e) {
            ctx.signalFailure(e);
            throw e;
        }
    }

    public CountingInputStream makeStream(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        InputStream buf = new BufferedInputStream(in);
        return new CountingInputStream(buf);
    }

    public void determineLength() throws IOException, DecodeException {
        CountingInputStream stream = null;
        final long fileLength = file.length();
        long count = 0;
        try {
            stream = makeStream(file);
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
                long read = stream.getCount();
                ctx.signalProgress((float) read / fileLength);
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
