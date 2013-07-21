package pl.edu.agh.ki.grieg.decoder.builtin.mp3;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.meta.Keys;
import pl.edu.agh.ki.grieg.meta.MetaInfo;
import pl.edu.agh.ki.grieg.meta.MetaKey;

import com.google.common.io.Closeables;

class MetaExtractor {

    private final File file;
    private final Set<MetaKey<?>> desired;
    private final MetaInfo info;

    public MetaExtractor(File file, Set<MetaKey<?>> desired) {
        this(file, desired, new MetaInfo());
    }

    public MetaExtractor(File file, Set<MetaKey<?>> desired, MetaInfo info) {
        this.file = file;
        this.info = info;
        this.desired = desired;
    }

    public MetaInfo extract() throws DecodeException, IOException {
        if (desired.contains(Keys.SAMPLES)) {
            determineLength();
            desired.remove(Keys.SAMPLES);
        }
        return info;
    }

    public void determineLength() throws IOException, DecodeException {
        InputStream stream = null;
        long count = 0;
        try {
            stream = new BufferedInputStream(new FileInputStream(file));
            FrameReader reader = new FrameReader(stream);
            for (Header header : reader) {
                SampleBuffer samples = reader.readSamples(header);
                int bufferSize = samples.getBufferLength();
                int channels = samples.getChannelCount();
                count += bufferSize / channels;
                reader.closeFrame();
            }
            info.put(Keys.SAMPLES, count);
        } catch (DecoderException e) {
            throw new DecodeException(e);
        } finally {
            Closeables.close(stream, true);
        }
    }

}
