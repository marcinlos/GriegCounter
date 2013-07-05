package pl.edu.agh.ki.grieg.core;

import java.io.IOException;

import pl.edu.agh.ki.grieg.data.Format;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.utils.iteratee.AbstractEnumerator;
import pl.edu.agh.ki.grieg.utils.iteratee.Iteratee;

public class AudioFileSampleEnumerator extends AbstractEnumerator<float[][]> implements SampleEnumerator {
    
    private static final int SIZE = 2048;
    
    private AudioFile file;

    public AudioFileSampleEnumerator(AudioFile file) {
        this.file = file;
    }
    
    public void run() throws AudioException, IOException {
        AudioStream stream = file.getStream();
        float[][] buffer = new float[getFormat().channels][SIZE];
        while (true) {
            stream.readSamples(buffer);
        }
    }

    @Override
    public Format getFormat() {
        return file.getDetails().getFormat();
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
    }

}
