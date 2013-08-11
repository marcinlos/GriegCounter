package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.analysis.HammingSegmenter;
import pl.edu.agh.ki.grieg.analysis.Segmenter;
import pl.edu.agh.ki.grieg.analysis.Skipper;
import pl.edu.agh.ki.grieg.analysis.WaveCompressor;
import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.features.AudioFeatures;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.Range;

/**
 * Default {@link PipelineAssembler} implementation. Creates few basic
 * processing nodes.
 * 
 * @author los
 * 
 */
public class DefaultPipelineAssembler implements PipelineAssembler {

    /**
     * Default number of samples to be emited in each batch of the hamming
     * segmenter
     */
    public static final int DEFAULT_CHUNK_SIZE = 2048;

    /** How many samples in each batch should overlap with the previous */
    public static final int DEFAULT_HOP_SIZE = 441;
    
    /** How many batches in total should the data be split into */
    public static final int DEFAULT_RESOLUTION = 10000;

    @Override
    public void build(Pipeline<float[][]> pipeline, Properties config,
            Properties audio) {

        long length = audio.get(AudioFeatures.SAMPLES);
        SoundFormat format = audio.get(AudioFeatures.FORMAT);
        int channels = format.getChannels();

        int resolution = config.getInt("resolution", 99);
        int chunkSize = config.getInt("chunk-size", DEFAULT_CHUNK_SIZE);
        int hopSize = config.getInt("hop-size", DEFAULT_HOP_SIZE);
        
        int packetSize = (int) (length / resolution);
        
        Segmenter segmenter = new Segmenter(channels, packetSize);
        
        pipeline.as("segmenter")
                .connect(segmenter, float[][].class, float[][].class)
                .toRoot();
        
        WaveCompressor compressor = new WaveCompressor(channels);

        pipeline.as("compressor")
                .connect(compressor, float[][].class, Range[].class)
                .to("segmenter");

        HammingSegmenter hamming = new HammingSegmenter(channels, hopSize, chunkSize);

        pipeline.as("hamming")
                .connect(hamming, float[][].class, float[][].class)
                .toRoot();
        
        Skipper skipper = new Skipper(packetSize);
        
        pipeline.as("skipper")
                .connect(skipper, float[][].class, float[].class)
                .toRoot(); 
        
    }

}
