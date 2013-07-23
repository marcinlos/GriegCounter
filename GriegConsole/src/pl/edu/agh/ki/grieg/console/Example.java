package pl.edu.agh.ki.grieg.console;

import java.io.File;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.meta.Keys;
import pl.edu.agh.ki.grieg.playback.PlaybackAdapter;
import pl.edu.agh.ki.grieg.playback.Player;
import pl.edu.agh.ki.grieg.playback.Timestamp;

public class Example {

    private static final String DIR = "/media/los/Data/muzyka/Klasyczna/";
    private static final String FILE = DIR + "Bach/Inventions/Invention no. 8 (F major).mp3";
    private static final String BEETH = DIR + "Beethoven/Op. 109 (PS no. 30 in E major)/Piano Sonata no.30 in E major op.109 - II- Prestissimo.mp3";
    private static final String BIG = DIR + "Beethoven/Beethoven's 9th.mp3";
    private static final String RACH = DIR + "Rachmaninov/Op. 28 (PS no. 1 in D minor)/03 Piano Sonata No.1 in D minor Op.28 - III. Allegro molto.mp3";
    private static final String WAV = "/home/los/Downloads/guitarup_fuller.wav";
    private static final String SCHUBERT = "/home/los/Downloads/Schubert - Serenade.wav";

    public static void main(String[] args) throws Exception {

        final FileLoader fileLoader = new FileLoader();
        final Player player = new Player(2048);

        File file = new File(FILE);

        //player.play(file);
        AudioFile audioFile = fileLoader.loadFile(file);

        {
            final long count = audioFile.determine(Keys.SAMPLES);
            System.out.println("Frames = " + count);
            player.addListener(new PlaybackAdapter() {
                @Override
                public void moved(Timestamp time) {
                    float percent = time.sample / (float) count;
                    System.out.printf("%.1f%% - %s\n", percent * 100, time);
                }
            });
            player.play(audioFile);
        }
        /*{
            Stopwatch stopwatch = new Stopwatch().start();
            Long count = audioFile.getInfo(Keys.SAMPLES);
            stopwatch.stop();

            System.out.println("Done in " + stopwatch);
            System.out.println("Total frames: " + count);
            
            int pieceSize = (int) (count / 20);
            SampleEnumerator source = audioFile.openSource();
            int channels = source.getFormat().channels;
            WaveCompressor wave = new WaveCompressor(channels, pieceSize);
            source.connect(wave);
            wave.connect(new AbstractIteratee<Range[]>() {
                @Override
                public State step(Range[] item) {
                    System.out.println(Arrays.toString(item));
                    return State.Cont;
                }
            });
            source.start();
        }*/
    }

}
