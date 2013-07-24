package pl.edu.agh.ki.grieg.processing.observers;

import static com.google.common.base.Preconditions.checkPositionIndex;

import java.util.List;
import java.util.Set;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.meta.AudioKeys;
import pl.edu.agh.ki.grieg.processing.core.ProcessingAdapter;
import pl.edu.agh.ki.grieg.processing.tree.ProcessingTree;
import pl.edu.agh.ki.grieg.utils.Key;
import pl.edu.agh.ki.grieg.utils.Properties;
import pl.edu.agh.ki.grieg.utils.Range;
import pl.edu.agh.ki.grieg.utils.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.utils.iteratee.State;

import com.google.common.collect.Lists;

public abstract class PCMObserver extends ProcessingAdapter implements
        Iteratee<Range[]> {

    private AudioFile file;
    
    private SoundFormat format;

    private long totalSampleCount;

    private final List<List<Range>> ranges = Lists.newArrayList();
    
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
    
    protected List<List<Range>> data() {
        return ranges;
    }
    
    protected int rangeCount() {
        return ranges.size();
    }
    
    @Override
    public void fileOpened(AudioFile file) {
        this.file = file;
    }

    @Override
    public void readingMetaInfo(Set<Key<?>> desired, Properties config) {
        desired.add(AudioKeys.SAMPLES);
        desired.add(AudioKeys.FORMAT);
    }

    @Override
    public void gatheredMetainfo(Properties info) {
        format = info.get(AudioKeys.FORMAT); 
        if (info.contains(AudioKeys.SAMPLES)) {
            totalSampleCount = info.get(AudioKeys.SAMPLES);
        } else {
            sampleCountMissing();
        }
    }

    @Override
    public void processingStarted(ProcessingTree<float[][]> tree) {
        tree.connect(this, Range[].class).to("compressor");
    }

    protected abstract void sampleCountMissing();
    
    protected List<Range> getChannel(int i) {
        checkPositionIndex(i, channels());
        return ranges.get(i);
    }

    @Override
    public State step(Range[] item) {
        for (int i = 0; i < channels(); ++ i) {
            getChannel(i).add(item[i]);
        }
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
