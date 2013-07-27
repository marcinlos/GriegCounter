package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.analysis.Segmenter;
import pl.edu.agh.ki.grieg.analysis.Skipper;
import pl.edu.agh.ki.grieg.analysis.WaveCompressor;
import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.meta.AudioKeys;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.utils.Properties;
import pl.edu.agh.ki.grieg.utils.Range;

public class DefaultPipelineFactory implements PipelineAssembler {

    public static final int RESOLUTION = 10000;
    
    public static final int CHUNK_SIZE = 2048;
    
    public static final int HOP_SIZE = 441;

    @Override
    public void build(Pipeline<float[][]> pipeline, Properties config,
            Properties audio) {

        long length = audio.get(AudioKeys.SAMPLES);
        SoundFormat format = audio.get(AudioKeys.FORMAT);
        int channels = format.getChannels();

        int packetSize = (int) (length / RESOLUTION);
        WaveCompressor compressor = new WaveCompressor(channels, packetSize);

        pipeline.as("compressor")
                .connect(compressor, float[][].class, Range[].class)
                .toRoot();

        Segmenter segmenter = new Segmenter(channels, HOP_SIZE, CHUNK_SIZE);

        pipeline.as("segmenter")
                .connect(segmenter, float[][].class, float[][].class)
                .toRoot();
        
        Skipper skipper = new Skipper(packetSize);
        
        pipeline.as("skipper")
                .connect(skipper, float[][].class, float[].class)
                .toRoot(); 
        
    }

}
