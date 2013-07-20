package pl.edu.agh.ki.grieg.example;

import java.io.File;
import java.io.IOException;

import pl.edu.agh.ki.grieg.analysis.SampleCounter;
import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.io.StreamSampleEnumerator;
import pl.edu.agh.ki.grieg.meta.Keys;
import pl.edu.agh.ki.grieg.playback.Player;

import com.google.common.base.Stopwatch;

public class Example {

    private static final String DIR = "/media/los/Data/muzyka/Klasyczna/";
    private static final String FILE = DIR + "Bach/Inventions/Invention no. 8 (F major).mp3";
    private static final String BEETH = DIR + "Beethoven/Op. 109 (PS no. 30 in E major)/Piano Sonata no.30 in E major op.109 - II- Prestissimo.mp3";
    private static final String BIG = DIR + "Beethoven/Beethoven's 9th.mp3";
    private static final String RACH = DIR + "Rachmaninov/Op. 28 (PS no. 1 in D minor)/03 Piano Sonata No.1 in D minor Op.28 - III. Allegro molto.mp3";
    private static final String WAV = "/home/los/Downloads/guitarup_fuller.wav";
    private static final String SCHUBERT = "/home/los/Downloads/Schubert - Serenade.wav";

    public static void main(String[] args) throws IOException, AudioException {

        final FileLoader fileLoader = FileLoader.getInstance();
        final Player player = new Player(fileLoader, 2048);

        File file = new File(RACH);

        // player.play(file);
        AudioFile audioFile = fileLoader.loadFile(file);
        AudioStream stream = audioFile.openStream();

        {
            SampleEnumerator source = new StreamSampleEnumerator(stream, 2048);

            SampleCounter counter = new SampleCounter();
            source.connect(counter);

            Stopwatch stopwatch = new Stopwatch().start();
            source.start();
            stopwatch.stop();

            System.out.println("Done in " + stopwatch);
            System.out.println("Total frames: " + counter.getFrameCount());
        }
        {
            Stopwatch stopwatch = new Stopwatch().start();
            Long count = audioFile.getInfo(Keys.SAMPLES);
            stopwatch.stop();

            System.out.println("Done in " + stopwatch);
            System.out.println("Total frames: " + count);
        }
    }

}
