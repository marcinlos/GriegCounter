package griegcounter.audio;

import java.io.File;

import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;

public class Track {

    private Sample sample;
    private File file;
    
    public Track(File file) {
        //SampleManager.setBufferingRegime(Sample.Regime.newStreamingRegime(10));
        //Sample.Regime regime = new Sample.TimedRegime(600000, 700000, 1000, 1, Sample.TimedRegime.Order.ORDERED);
        //SampleManager.setBufferingRegime(regime);
        sample = SampleManager.sample(file.getPath());
        if (sample == null) {
            throw new IllegalStateException("null sample");
        }
        this.file = file;
    }
    
    public Sample getSample() {
        return sample;
    }
    
    public File getFile() {
        return file;
    }
    
    public float[] allocBuffer() {
        return new float[sample.getNumChannels()];
    }
    
    public int channels() {
        return sample.getNumChannels();
    }
    
    public long frames() {
        return sample.getNumFrames();
    }

}
