package pl.edu.agh.ki.grieg.processing.model;

import java.util.Set;

import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.processing.core.ProcessingListener;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.Key;
import pl.edu.agh.ki.grieg.util.Properties;

public class PowerObserver implements ProcessingListener {


    @Override
    public void fileOpened(AudioFile file, Properties config) {
        
    }

    @Override
    public void beforePreAnalysis(Set<Key<?>> desired, Properties config) {
        
    }

    @Override
    public void afterPreAnalysis(Properties results) {
        
    }

    @Override
    public void beforeAnalysis(Pipeline<float[][]> pipeline) {
        //pipeline.connect(this, Range[].class).to("compressor");
    }

    @Override
    public void afterAnalysis() {
        
    }

    @Override
    public void failed(Throwable e) {
        
    }

}