package pl.edu.agh.ki.grieg.io;

import java.io.DataInputStream;
import java.io.IOException;

import pl.edu.agh.ki.grieg.data.Format;
import pl.edu.agh.ki.grieg.data.SourceDetails;

public abstract class DecodingAudioStream implements AudioStream {

    private DataInputStream stream;
    private SourceDetails details;

    public DecodingAudioStream(DataInputStream stream, SourceDetails details) {
        this.stream = stream;
        this.details = details;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

    @Override
    public int readSamples(float[][] buffer) throws AudioException, IOException {
        int count = buffer.length;
        int i;
        for (i = 0; i < count && readSingleSample(buffer, i); ++ i);
        return i;
    }

    protected abstract boolean readSingleSample(float[][] samples, int n)
            throws AudioException, IOException;

    @Override
    public Format getFormat() {
        return details.getFormat();
    }
    
    protected DataInputStream stream() {
        return stream;
    }

}
