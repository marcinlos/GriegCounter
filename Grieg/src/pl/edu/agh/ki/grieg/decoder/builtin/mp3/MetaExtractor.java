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
import pl.edu.agh.ki.grieg.meta.AudioKeys;
import pl.edu.agh.ki.grieg.utils.Key;
import pl.edu.agh.ki.grieg.utils.Properties;
import pl.edu.agh.ki.grieg.utils.TypedMap;

import com.google.common.io.Closeables;

class MetaExtractor {

    private final File file;
    private final Set<Key<?>> desired;
    private final Properties info;

    public MetaExtractor(File file, Set<Key<?>> desired) {
        this(file, desired, new Properties());
    }

    public MetaExtractor(File file, Set<Key<?>> desired, Properties info) {
        this.file = file;
        this.info = info;
        this.desired = desired;
    }

    public TypedMap extract() throws DecodeException, IOException {
        if (desired.contains(AudioKeys.SAMPLES)) {
            determineLength();
            desired.remove(AudioKeys.SAMPLES);
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
            info.put(AudioKeys.SAMPLES, count);
        } catch (DecoderException e) {
            throw new DecodeException(e);
        } finally {
            Closeables.close(stream, true);
        }
    }

}
