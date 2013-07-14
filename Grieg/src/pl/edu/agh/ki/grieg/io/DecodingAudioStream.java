package pl.edu.agh.ki.grieg.io;

import java.io.IOException;

import pl.edu.agh.ki.grieg.data.Format2;
import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.utils.BinaryInputStream;

public abstract class DecodingAudioStream implements AudioStream {

    private BinaryInputStream stream;
    private SourceDetails details;

    private long remains;

    public DecodingAudioStream(BinaryInputStream stream, SourceDetails details) {
        this.stream = stream;
        this.details = details;
        this.remains = details.getSampleCount();
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

    @Override
    public int readSamples(float[][] buffer) throws AudioException, IOException {
        int count = buffer[0].length;
        int i;
        for (i = 0; --remains >= 0 && i < count
                && readSingleSample(buffer, i++);)
            ;
        return i;
    }

    protected abstract boolean readSingleSample(float[][] samples, int n)
            throws AudioException, IOException;

    @Override
    public Format2 getFormat() {
        return details.getFormat();
    }

    protected BinaryInputStream stream() {
        return stream;
    }

}
