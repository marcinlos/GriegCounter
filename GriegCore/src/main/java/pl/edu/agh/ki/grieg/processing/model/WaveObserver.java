package pl.edu.agh.ki.grieg.processing.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.features.AudioFeatures;
import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.processing.core.ProcessingAdapter;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;
import pl.edu.agh.ki.grieg.util.properties.Properties;

public abstract class WaveObserver extends ProcessingAdapter implements
        Iteratee<float[]> {

    private static final Logger logger = LoggerFactory
            .getLogger(WaveObserver.class);

    private AudioFile file;

    private SoundFormat format;

    private long totalSampleCount;

    private int rangeCount;

    private int resolution;

    protected AudioFile file() {
        return file;
    }

    protected long totalSampleCount() {
        return totalSampleCount;
    }

    protected SoundFormat format() {
        return format;
    }

    protected int channels() {
        return format.channels;
    }

    protected int rangeCount() {
        return rangeCount;
    }

    protected int resolution() {
        return resolution;
    }

    protected float progress() {
        return rangeCount() / (float) resolution;
    }

    @Override
    public void fileOpened(AudioFile file, Properties config) {
        this.file = file;
        resolution = config.getInt("resolution");
        logger.debug("Resolution={}", resolution);
    }

    @Override
    public void beforePreAnalysis(ExtractionContext ctx) {

        logger.debug("Before pre-analysis, requesting SAMPLES and FORMAT");
        ctx.requestFeatures(AudioFeatures.SAMPLES, AudioFeatures.FORMAT);
    }

    @Override
    public void afterPreAnalysis(Properties results) {
        format = results.get(AudioFeatures.FORMAT);
        logger.debug("After preanalysis, format={}", format);
        if (results.contains(AudioFeatures.SAMPLES)) {
            totalSampleCount = results.get(AudioFeatures.SAMPLES);
            logger.debug("Total {} samples", totalSampleCount);
        } else {
            logger.warn("Missing property - total sample count");
            sampleCountMissing();
        }
    }

    @Override
    public void beforeAnalysis(Pipeline<float[][]> pipeline,
            SampleEnumerator source) {
        pipeline.connect(this, float[].class).to("skipper");
    }

    protected abstract void sampleCountMissing();

    @Override
    public State step(float[] item) {
        ++rangeCount;
        return State.Cont;
    }

    @Override
    public void finished() {
        reset();
    }

    @Override
    public void failed(Throwable e) {
        reset();
    }

    protected void reset() {
        logger.debug("Resetting");
        file = null;
        format = null;
        totalSampleCount = -1;
        rangeCount = 0;
    }

}
