package pl.edu.agh.ki.grieg.playback;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import pl.edu.agh.ki.grieg.data.Format2;
import pl.edu.agh.ki.grieg.decoder.util.PCM;

public class Player {

    private SourceDataLine line;

    private Format2 fmt;

    public Player(Format2 fmt) throws LineUnavailableException {
        this.fmt = fmt;
        AudioFormat format = new AudioFormat(fmt.sampleRate, 16, fmt.channels,
                true, true);
        System.out.println(format);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format);
    }

    public void start() {
        line.start();
        System.out.println(line.getLevel());
    }

    public void write(float[][] data) {
        write(data, 0, data[0].length);
    }

    public void write(float[][] data, int start, int length) {
        byte[] buffer = new byte[length * fmt.channels * 2];
        ShortBuffer shorts = ByteBuffer.wrap(buffer)
                .order(ByteOrder.BIG_ENDIAN).asShortBuffer();
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < fmt.channels; ++j) {
                shorts.put(PCM.toShort(data[j][start + i]));
            }
        }
        line.write(buffer, 0, buffer.length);
    }

    public void close() {
        line.drain();
        line.stop();
        line.close();
    }

}
