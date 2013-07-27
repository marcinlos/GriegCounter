package pl.edu.agh.ki.grieg.processing.observers;

import static com.google.common.base.Preconditions.checkPositionIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.meta.AudioKeys;
import pl.edu.agh.ki.grieg.processing.core.ProcessingAdapter;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.utils.Key;
import pl.edu.agh.ki.grieg.utils.Properties;
import pl.edu.agh.ki.grieg.utils.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.utils.iteratee.State;

import com.google.common.collect.Lists;

public abstract class WaveObserver extends ProcessingAdapter implements
        Iteratee<float[]> {

    private AudioFile file;

    private SoundFormat format;

    private long totalSampleCount;

    private final List<List<Float>> points = Lists.newArrayList();
    
    private int rangeCount;

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

    protected List<List<Float>> data() {
        return points;
    }

    protected int rangeCount() {
        return rangeCount;
    }
    
    protected float progress() {
        return rangeCount() / 10000.0f;
    }

    @Override
    public void fileOpened(AudioFile file) {
        this.file = file;
    }

    @Override
    public void beforePreAnalysis(Set<Key<?>> desired, Properties config) {
        desired.add(AudioKeys.SAMPLES);
        desired.add(AudioKeys.FORMAT);
    }

    @Override
    public void afterPreAnalysis(Properties results) {
        format = results.get(AudioKeys.FORMAT);
        for (int i = 0; i < format.channels; ++i) {
            points.add(new ArrayList<Float>());
        }
        if (results.contains(AudioKeys.SAMPLES)) {
            totalSampleCount = results.get(AudioKeys.SAMPLES);
        } else {
            sampleCountMissing();
        }
    }

    @Override
    public void beforeAnalysis(Pipeline<float[][]> tree) {
        tree.connect(this, float[].class).to("skipper");
    }

    protected abstract void sampleCountMissing();

    protected List<Float> getChannel(int i) {
        checkPositionIndex(i, channels());
        return points.get(i);
    }

    @Override
    public State step(float[] item) {
        for (int i = 0; i < item.length; ++i) {
            getChannel(i).add(item[i]);
        }
        ++ rangeCount;
        return State.Cont;
    }

    @Override
    public void finished() {
        // empty
    }

    @Override
    public void failed(Throwable e) {
        // empty
    }

}
