package griegcounter.audio;

import net.beadsproject.beads.analysis.FeatureExtractor;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.WavePlayer;

public class Player {

    private AudioContext ac = new AudioContext();
    private SamplePlayer player;
    private Gain gain;
    private ShortFrameSegmenter sfs;
    
    // These are probably important!
    private int chunkSize = 2048;
    private int hopSize = 441;
    
    private float gainValue = 0.6f;

    public Player() {
        gain = new Gain(ac, 2, gainValue);
        ac.out.addInput(gain);
        sfs = new ShortFrameSegmenter(ac);
        sfs.setChunkSize(chunkSize);
        sfs.setHopSize(hopSize);
        sfs.addInput(ac.out);
        ac.out.addDependent(sfs);
    }

    public void play(Track track) {
        stop();
        player = new SamplePlayer(ac, track.getSample());
        gain.addInput(player);
        ac.start();
    }
    
    public void playSineWave(float freq) {
        ac.stop();
        gain.addInput(new WavePlayer(ac, freq, Buffer.SINE));
        ac.start();
    }
    
    public void stop() {
        if (player != null) {
            player.kill();
        }
    }
    
    public int getChunkSize() {
        return chunkSize;
    }
    
    public int getHopSize() {
        return hopSize;
    }
    
    public AudioContext getContext() {
        return ac;
    }
    
    public void addAnalysis(FeatureExtractor<?, float[]> analysis) {
        sfs.addListener(analysis);
    }

}
