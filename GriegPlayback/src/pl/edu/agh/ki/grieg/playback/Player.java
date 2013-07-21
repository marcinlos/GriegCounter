package pl.edu.agh.ki.grieg.playback;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.io.StreamSampleEnumerator;
import pl.edu.agh.ki.grieg.utils.iteratee.AbstractIteratee;
import pl.edu.agh.ki.grieg.utils.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.utils.iteratee.State;

import com.google.common.collect.Lists;

public class Player {

    /**
     * How many times per second are {@code PlaybackListener}s notified about
     * the progress by default
     */
    private static final int DEFAULT_NOTIFY_RATE = 25;

    /** Used to load audio files */
    private final FileLoader loader;

    /** List of listeners */
    private final List<PlaybackListener> listeners = Lists.newArrayList();

    /** Track currently being played */
    private TrackPlayback currentPlayback;

    /** Creates outputs */
    private final OutputFactory outputFactory = new OutputFactory();

    /** Size of the audio data buffer (in samples) */
    private final int bufferSize;

    /**
     * How many times per second are {@code PlaybackListener}s notified about
     * the progress
     */
    private final int notifyRate;

    /** Worker pool used to asynchronously play audio */
    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    /**
     * Listens to notifications form {@link ProgressNotifier} and forwards to
     * the {@link PlaybackListener}s
     */
    private final Iteratee<Timestamp> progressForwarder = new AbstractIteratee<Timestamp>() {
        @Override
        public State step(Timestamp time) {
            signalProgress(time);
            return State.Cont;
        }
    };

    /**
     * Creates new Player with specified configuration parameters.
     * 
     * @param loader
     *            {@link FileLoader} used to load audio files
     * @param bufferSize
     *            Size of the audio output buffers
     * @param notifyRate
     *            How often are the {@code PlaybackListener}s notified about the
     *            playback progress
     */
    public Player(FileLoader loader, int bufferSize, int notifyRate) {
        this.loader = loader;
        this.bufferSize = bufferSize;
        this.notifyRate = notifyRate;
    }

    /**
     * Creates new Player with specified configuration parameters and default
     * progress notify rate.
     * 
     * @param loader
     *            {@link FileLoader} used to load audio files
     * @param bufferSize
     *            Size of the audio output buffers
     */
    public Player(FileLoader loader, int bufferSize) {
        this(loader, bufferSize, DEFAULT_NOTIFY_RATE);
    }

    /**
     * Attaches new playback events listener
     * 
     * @param listener
     *            Listener to be added to the list
     */
    public synchronized void addListener(PlaybackListener listener) {
        listeners.add(listener);
    }

    /**
     * Detaches playback events listener
     * 
     * @param listener
     *            Listener to be removed from the list
     */
    public synchronized void removeListener(PlaybackListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all the listeners when playback begins
     */
    private synchronized void signalStart() {
        for (PlaybackListener listener : listeners) {
            listener.started();
        }
    }

    /**
     * Notifies all the listeners when the playback ends
     */
    private synchronized void signalStop() {
        for (PlaybackListener listener : listeners) {
            listener.stopped();
        }
    }

    /**
     * Notifies all the listeners when the playback is paused
     */
    private synchronized void signalPause() {
        for (PlaybackListener listener : listeners) {
            listener.paused();
        }
    }

    /**
     * Notifies all the listeners when the playback is resumed
     */
    private synchronized void signalResume() {
        for (PlaybackListener listener : listeners) {
            listener.resumed();
        }
    }

    /**
     * Notifies all the listeners when an error occurs
     * 
     * @param e
     *            Exception to inform listeners about
     */
    private synchronized void signalFailure(Exception e) {
        for (PlaybackListener listener : listeners) {
            listener.failed(e);
        }
    }

    /**
     * Notifies all the listeners about the playback progress
     * 
     * @param time
     *            Current time offset of the playback
     */
    private synchronized void signalProgress(Timestamp time) {
        for (PlaybackListener listener : listeners) {
            listener.moved(time);
        }
    }

    /**
     * @return Currently active playback object, or {@code null} if there is
     *         none
     */
    public TrackPlayback getCurrentPlayback() {
        return currentPlayback;
    }

    /**
     * Pauses the playback. It does nothing if there is no current playback.
     */
    public void pause() {
        if (currentPlayback != null) {
            currentPlayback.pause();
            signalPause();
        }
    }

    /**
     * Resumes the playback. It does nothing if there is no current playback.
     */
    public void resume() {
        if (currentPlayback != null) {
            currentPlayback.resume();
            signalResume();
        }
    }

    /**
     * Stops the playback. It does nothing if there is no current playback.
     */
    public void stop() {
        if (currentPlayback != null) {
            currentPlayback.stop();
            signalStop();
        }
    }

    /**
     * Initiates player shutdown. Currently played track will be interrupted.
     * Waits 5 seconds for player termination.
     * 
     * @throws InterruptedException
     *             If interrupted while waiting
     */
    public void shutdown() throws InterruptedException {
        stop();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MILLISECONDS);
    }

    /**
     * Initiates player shutdown. Currently played track will be interrupted.
     * Waits specified amount of time for player termination.
     * 
     * @throws InterruptedException
     *             If interrupted while waiting
     */
    public void shutdown(long timeout, TimeUnit unit)
            throws InterruptedException {
        stop();
        executor.shutdown();
        executor.awaitTermination(timeout, unit);
    }

    public void play(String path) throws IOException, AudioException {
        play(new File(path));
    }

    public void play(File file) throws IOException, AudioException {
        AudioFile audioFile = loader.loadFile(file);
        play(audioFile);
    }

    public void play(AudioFile file) throws IOException, AudioException {
        AudioStream stream = file.openStream();
        SampleEnumerator source = new StreamSampleEnumerator(stream, bufferSize);
        play(source);
    }

    public void play(SampleEnumerator source) throws AudioException,
            IOException {
        stop();
        AudioOutput output = outputFactory.boundTo(source);
        currentPlayback = new TrackPlayback(output, source);
        int sampleRate = source.getFormat().getSampleRate();
        ProgressNotifier notifier = new ProgressNotifier(sampleRate, notifyRate);
        notifier.connect(progressForwarder);
        currentPlayback.connect(notifier);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    currentPlayback.start();
                } catch (AudioException e) {
                    signalFailure(e);
                } catch (IOException e) {
                    signalFailure(e);
                }
            }
        });
        signalStart();
    }

}
