package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.analysis.Multiplexer;
import pl.edu.agh.ki.grieg.analysis.FFT;
import pl.edu.agh.ki.grieg.analysis.HammingSegmenter;
import pl.edu.agh.ki.grieg.analysis.Power;
import pl.edu.agh.ki.grieg.analysis.PowerSpectrum;
import pl.edu.agh.ki.grieg.analysis.Segmenter;
import pl.edu.agh.ki.grieg.analysis.Skipper;
import pl.edu.agh.ki.grieg.analysis.WaveCompressor;
import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.features.AudioFeatures;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.math.Range;
import pl.edu.agh.ki.grieg.util.properties.Properties;

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

        int resolution = config.getInt("resolution", DEFAULT_RESOLUTION);
        int chunkSize = config.getInt("chunk-size", DEFAULT_CHUNK_SIZE);
        int hopSize = config.getInt("hop-size", DEFAULT_HOP_SIZE);
        
        int packetSize = Math.max((int) (length / resolution), 1);
        
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
        
        Power power = new Power();
        
        pipeline.as("power")
                .connect(power, float[][].class, float[].class)
                .to("segmenter");
        
        Multiplexer<float[]> hammingLeft = Multiplexer.choose(0);
        
        pipeline.as("hamming_left")
                .connect(hammingLeft, float[][].class, float[].class)
                .to("hamming");
        
        FFT fft = new FFT();
        
        pipeline.as("fft")
                .connect(fft, float[].class, float[][].class)
                .to("hamming_left");
        
        Multiplexer<float[]> fftReal = Multiplexer.choose(0);
        pipeline.as("fft_real")
                .connect(fftReal, float[][].class, float[].class)
                .to("fft");
        
        Multiplexer<float[]> fftImag = Multiplexer.choose(1);
        pipeline.as("fft_imag")
                .connect(fftImag, float[][].class, float[].class)
                .to("fft");
        
        PowerSpectrum powerSpectrum = new PowerSpectrum();
        pipeline.as("power_spectrum")
                .connect(powerSpectrum, float[][].class, float[].class)
                .to("fft");
        
    }

}
