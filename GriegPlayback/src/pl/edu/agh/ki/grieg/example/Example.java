package pl.edu.agh.ki.grieg.example;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.io.StreamSampleEnumerator;
import pl.edu.agh.ki.grieg.playback.Player;
import pl.edu.agh.ki.grieg.utils.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.utils.iteratee.State;

public class Example {

    private static FileLoader loader = FileLoader.getInstance();

    private static final String DIR = "/media/los/Data/muzyka/Klasyczna/";
    private static final String FILE = DIR + "Bach/Inventions/Invention no. 8 (F major).mp3";
    private static final String BEETH = DIR + "Beethoven/Op. 109 (PS no. 30 in E major)/Piano Sonata no.30 in E major op.109 - II- Prestissimo.mp3";
    private static final String BIG = DIR + "Beethoven/Beethoven's 9th.mp3";
    private static final String RACH = DIR + "Rachmaninov/Op. 28 (PS no. 1 in D minor)/03 Piano Sonata No.1 in D minor Op.28 - III. Allegro molto.mp3";
    private static final String WAV = "/home/los/Downloads/guitarup_fuller.wav";
    private static final String SCHUBERT = "/home/los/Downloads/Schubert - Serenade.wav";

    public static void main(String[] args) throws IOException, AudioException,
            LineUnavailableException {
        File file = new File(RACH);
        AudioFile audio = loader.loadFile(file);
        AudioStream stream = audio.getStream();
        SourceDetails details = audio.getDetails();
        System.out.println(details);
        SoundFormat format = details.getFormat();

        final Player player = new Player(format);
        player.start();
        SampleEnumerator enumerator = new StreamSampleEnumerator(stream, 100);
        enumerator.connect(new Iteratee<float[][]>() {
            
            @Override
            public State step(float[][] item) {
                player.write(item);
                return State.Cont;
            }

            @Override
            public void finished() {
                System.out.println("Done!");
            }

            @Override
            public void failed(Throwable e) {
                e.printStackTrace(System.err);
            }
        });
        enumerator.start();
        player.close();
    }

}
