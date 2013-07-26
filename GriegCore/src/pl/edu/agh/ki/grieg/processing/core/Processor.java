package pl.edu.agh.ki.grieg.processing.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import pl.edu.agh.ki.grieg.analysis.Segmenter;
import pl.edu.agh.ki.grieg.analysis.Skipper;
import pl.edu.agh.ki.grieg.analysis.WaveCompressor;
import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.meta.AudioKeys;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.utils.Key;
import pl.edu.agh.ki.grieg.utils.PropertyMap;
import pl.edu.agh.ki.grieg.utils.Properties;
import pl.edu.agh.ki.grieg.utils.Range;

import com.google.common.collect.Sets;

public class Processor {

    private Pipeline<float[][]> tree;

    private final FileLoader loader;
    
    private final Environment env = new Environment();

    /** Collection of listeners */
    private final ProcessingListenerList listeners = new ProcessingListenerList();

    public Processor(FileLoader loader) {
        this.loader = checkNotNull(loader);
    }

    /**
     * Looks up suitable parser for the file and uses it to load the file.
     * 
     * @param file
     *            Audio file to open
     */
    public void openFile(File file) {
        try {
            AudioFile audioFile = loader.loadFile(file);
            env.setFile(audioFile);
            listeners.fileOpened(audioFile);
        } catch (AudioException e) {
            listeners.failed(e);
        } catch (IOException e) {
            listeners.failed(e);
        }
    }

    public void gatherMetadata() {
        try {
            final Set<Key<?>> keys = Sets.newHashSet();
            final Properties config = new PropertyMap();
            listeners.readingMetaInfo(keys, config);
            Properties info = env.getFile().computeAll(keys, config);
            listeners.gatheredMetainfo(info);
        } catch (AudioException e) {
            listeners.failed(e);
        } catch (IOException e) {
            listeners.failed(e);
        }
    }

    public void analyze() {
        try {
            buildTree();
            SampleEnumerator source = env.getFile().openSource();
            source.connect(tree);
            listeners.processingStarted(tree);
            source.start();
        } catch (AudioException e) {
            listeners.failed(e);
        } catch (IOException e) {
            listeners.failed(e);
        }
    }

    private void buildTree() {
        tree = Pipeline.make(float[][].class);
        long length = env.getFile().get(AudioKeys.SAMPLES);
        SoundFormat format = env.getFile().get(AudioKeys.FORMAT);
        int channels = format.getChannels();
        int packetSize = (int) (length / 10000);
        WaveCompressor compressor = new WaveCompressor(channels, packetSize);

        tree.as("compressor")
            .connect(compressor, float[][].class, Range[].class)
            .toRoot();

        Segmenter segmenter = new Segmenter(channels, 441, 2048);

        tree.as("segmenter")
            .connect(segmenter, float[][].class, float[][].class)
            .toRoot();
        
        Skipper skipper = new Skipper(packetSize);
        
        tree.as("skipper")
            .connect(skipper, float[][].class, float[].class)
            .toRoot();
    }

    /**
     * @return Audio file being processed
     */
    public AudioFile getFile() {
        return env.getFile();
    }

    /**
     * Registers new processing listener
     * 
     * @param listener
     *            Listener to be added to the list
     */
    public void addListener(ProcessingListener listener) {
        listeners.add(listener);
    }

    /**
     * Registers a sequence of listeners
     * 
     * @param iterable
     *            Sequence of listeners
     */
    public void addAll(Iterable<? extends ProcessingListener> iterable) {
        for (ProcessingListener listener : iterable) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a listener from the list
     * 
     * @param listener
     *            Listener to be disconnected
     */
    public void removeListener(ProcessingListener listener) {
        listeners.remove(listener);
    }

}
