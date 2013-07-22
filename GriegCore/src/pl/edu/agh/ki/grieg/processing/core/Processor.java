package pl.edu.agh.ki.grieg.processing.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.meta.MetaInfo;
import pl.edu.agh.ki.grieg.meta.MetaKey;
import pl.edu.agh.ki.grieg.playback.PlaybackListener;
import pl.edu.agh.ki.grieg.playback.Player;
import pl.edu.agh.ki.grieg.processing.tree.FlowTree;

import com.google.common.collect.Sets;

public class Processor {

    private final FlowTree<float[][]> tree;

    private final FileLoader loader;

    private final Player player;

    private AudioFile audioFile;

    /** Collection of listeners */
    private final ProcessingListenerList listeners = new ProcessingListenerList();

    public Processor(FlowTree<float[][]> tree, FileLoader loader, Player player) {
        this.tree = checkNotNull(tree);
        this.player = checkNotNull(player);
        this.loader = checkNotNull(loader);
    }

    public Processor(FlowTree<float[][]> tree, FileLoader loader) {
        this(tree, loader, new Player(loader));
    }

    public Processor(FlowTree<float[][]> tree) {
        this(tree, FileLoader.getInstance());
    }

    /**
     * Conducts the whole analysis of the audio file
     * 
     * @param file
     *            Audio file to process
     */
    public void process(File file) {
        try {
            // open the file
            audioFile = loader.loadFile(file);
            listeners.fileOpened(audioFile);

            // gather the metadata
            final Set<MetaKey<?>> keys = Sets.newHashSet();
            listeners.readingMetaInfo(keys);
            MetaInfo info = audioFile.getAll(keys);
            listeners.gatheredMetainfo(info);
            
            // actual processing
            SampleEnumerator source = audioFile.openSource();
            source.connect(tree);

        } catch (AudioException e) {
            listeners.failed(e);
        } catch (IOException e) {
            listeners.failed(e);
        }
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
     * Removes a listener from the list
     * 
     * @param listener
     *            Listener to be disconnected
     */
    public void removeListener(ProcessingListener listener) {
        listeners.remove(listener);
    }

    public void addPlaybackListener(PlaybackListener listener) {
        player.addListener(listener);
    }

    public void removePlaybackListener(PlaybackListener listener) {
        player.removeListener(listener);
    }

}
