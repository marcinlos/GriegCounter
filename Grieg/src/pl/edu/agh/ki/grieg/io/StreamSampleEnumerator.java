package pl.edu.agh.ki.grieg.io;

import java.io.IOException;

import pl.edu.agh.ki.grieg.data.Format;
import pl.edu.agh.ki.grieg.utils.iteratee.AbstractEnumerator;

public class StreamSampleEnumerator extends AbstractEnumerator<float[][]>
        implements SampleEnumerator {

    private AudioStream stream;
    private volatile boolean pause = false;

    private float[][] buffer;
    private int size;

    public StreamSampleEnumerator(AudioStream stream, int bufferSize) {
        this.stream = stream;
        this.size = bufferSize;
        buffer = makeBuffer(size);
    }

    private float[][] makeBuffer(int bufferSize) {
        int channels = getFormat().channels;
        return new float[channels][bufferSize];
    }

    private void copyBuffer(float[][] src, float[][] dst, int length) {
        for (int j = 0; j < getFormat().channels; ++j) {
            System.arraycopy(src[j], 0, dst[j], 0, length);
        }
    }

    @Override
    public Format getFormat() {
        return stream.getFormat();
    }

    @Override
    public void start() throws AudioException, IOException {
        int read;
        while ((read = stream.readSamples(buffer)) > 0) {
            if (read < size) {
                float[][] newBuf = makeBuffer(read);
                copyBuffer(buffer, newBuf, read);
                pushChunk(newBuf);
            } else {
                pushChunk(buffer);
            }
        }
        endOfStream();
        stream.close();
    }

    @Override
    public void pause() {
        pause = true;
    }

    @Override
    public void resume() {
        pause = false;
    }

    @Override
    public void stop() {
        pause();
    }

}
