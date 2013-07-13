package pl.edu.agh.ki.grieg.example;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.data.Format;
import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.playback.Player;

public class Example {

    private static FileLoader loader = FileLoader.getInstance();

    public static void main(String[] args) throws IOException, AudioException,
            LineUnavailableException {
        // File file = new File("/home/los/Downloads/Schubert - Serenade.wav");
        File file = new File("/home/los/Downloads/guitarup_fuller.wav");
        AudioFile audio = loader.loadFile(file);
        AudioStream stream = audio.getStream();
        SourceDetails details = audio.getDetails();
        System.out.println(details);
        Format format = details.getFormat();
        float[][] buffer = new float[format.channels][65536];

        Player player = new Player(format);
        player.start();
        while (true) {
            int read;
            if ((read = stream.readSamples(buffer)) > 0) {
                player.write(buffer, 0, read);
                System.out.println("I write " + read);
            } else {
                break;
            }
        }
        player.close();
    }

}
