package pl.edu.agh.ki.grieg.meta;

import static pl.edu.agh.ki.grieg.utils.Keys.make;
import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.utils.Key;

/**
 * Static utilities for dealing with metainfo keys
 * 
 * @author los
 */
public final class AudioKeys {

    private AudioKeys() {
        // non-instantiable
    }

    /** Number of PCM samples in the whole audio file */
    public static final Key<Long> SAMPLES = make("samples", Long.class);

    /** Guess at number of samples in the whole audio file */
    public static final Key<Long> APPROX_SAMPLES = make("approx_samples",
            Long.class);

    /** Duration (in seconds) of the audio */
    public static final Key<Float> DURATION = make("duration", Float.class);

    /** Format of the audio sound */
    public static final Key<SoundFormat> FORMAT = make("format",
            SoundFormat.class);

    /** Author */
    public static final Key<String> AUTHOR = make("author", String.class);

    /** Size of the file */
    public static final Key<Long> FILE_SIZE = make("file_size", Long.class);

}
