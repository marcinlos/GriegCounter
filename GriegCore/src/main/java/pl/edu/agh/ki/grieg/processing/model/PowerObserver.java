package pl.edu.agh.ki.grieg.processing.model;

import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.processing.core.ProcessingListener;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.properties.Properties;

public class PowerObserver implements ProcessingListener {


    @Override
    public void fileOpened(AudioFile file, Properties config) {
        
    }

    @Override
    public void beforePreAnalysis(ExtractionContext ctx) {
        
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
