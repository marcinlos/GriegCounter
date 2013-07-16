package pl.edu.agh.ki.grieg.example;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.io.StreamSampleEnumerator;
import pl.edu.agh.ki.grieg.playback.AudioOutput;

public class Example {

    private static FileLoader loader = FileLoader.getInstance();

    private static final String DIR = "/media/los/Data/muzyka/Klasyczna/";
    private static final String FILE = DIR + "Bach/Inventions/Invention no. 8 (F major).mp3";
    private static final String BEETH = DIR + "Beethoven/Op. 109 (PS no. 30 in E major)/Piano Sonata no.30 in E major op.109 - II- Prestissimo.mp3";
    private static final String BIG = DIR + "Beethoven/Beethoven's 9th.mp3";
    private static final String RACH = DIR + "Rachmaninov/Op. 28 (PS no. 1 in D minor)/03 Piano Sonata No.1 in D minor Op.28 - III. Allegro molto.mp3";
    private static final String WAV = "/home/los/Downloads/guitarup_fuller.wav";
    private static final String SCHUBERT = "/home/los/Downloads/Schubert - Serenade.wav";

    public static void main(String[] args) throws IOException, AudioException {
        File file = new File(WAV);
        AudioFile audio = loader.loadFile(file);
        AudioStream stream = audio.openStream();
        SoundFormat format = stream.getFormat();

        final AudioOutput player = new AudioOutput(format);
        final SampleEnumerator enumerator = new StreamSampleEnumerator(stream,
                100);

        enumerator.connect(player);
        player.start();

        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    enumerator.pause();
                    TimeUnit.SECONDS.sleep(3);
                    enumerator.resume();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, 3000);

        enumerator.start();
        // enumerator causes the output to close
        System.out.println("Done");
    }

}
