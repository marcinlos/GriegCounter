package griegcounter.test;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;

public class Main {

    private final static String FILE = "/media/los/Data/muzyka/Klasyczna/Bach/Inventions/Invention no. 8 (F major).mp3";

    public static void printData(Sample sample) {
        System.out.println("Length: " + sample.getLength());
        System.out.println("Bytes/sample: " + sample.getBytesPerSample());
        System.out.println("Channels: " + sample.getNumChannels());
        System.out.println("Frames: " + sample.getNumFrames());
        float[] data = new float[2];
        double[] sum = new double[2];
        long count = sample.getNumFrames();
        for (int i = 0; i < count; ++ i) {
            sample.getFrame(i, data);
            sum[0] += data[0];
            sum[1] += data[1];
        }
        double[] avg = { sum[0] / count, sum[1] / count };
        System.out.printf("Avg: %f : %f\n", avg[0], avg[1]);
    }
    
    
    
    public static void main(String[] args) {

        AudioContext ac = new AudioContext();
        Sample sample = SampleManager.sample(FILE);
        printData(sample);
        SamplePlayer player = new SamplePlayer(ac, sample);
        Gain g = new Gain(ac, 2, 0.2f);
        g.addInput(player);
        ac.out.addInput(g);
        ac.start();
    }

}
