package pl.edu.agh.ki.grieg.playback;

import javax.sound.sampled.LineUnavailableException;

import pl.edu.agh.ki.grieg.data.Format;

public class Main {

    private static final int RATE = 44100;

    public static void main(String[] args) throws LineUnavailableException,
            InterruptedException {
        // TODO Auto-generated method stub

        Format format = new Format(2, 16, RATE);
        Player player = new Player(format);
        player.start();
        for (int i = 0; i < 10; ++i) {
            float[][] buffer = new float[2][RATE];
            for (int j = 0; j < RATE; ++j) {
                float t = j / (float) RATE;
                float val = (float) Math.sin(2 * Math.PI * 440 * t);
                buffer[0][j] = buffer[1][j] = val;
            }
            player.write(buffer);
            System.out.println("dupa");
        }
        player.close();

    }
}
