package pl.edu.agh.ki.grieg.meta;

import pl.edu.agh.ki.grieg.data.SoundFormat;

/**
 * Static utilities for dealing with metainfo keys
 * 
 * @author los
 */
public final class Keys {

    private Keys() {
        // non-instantiable
    }

    /** Number of PCM samples in the whole audio file */
    public static final MetaKey<Long> SAMPLES = make("samples", Long.class);

    /** Guess at number of samples in the whole audio file */
    public static final MetaKey<Long> APPROX_SAMPLES = make("approx_samples",
            Long.class);

    /** Duration (in seconds) of the audio */
    public static final MetaKey<Float> DURATION = make("duration", Float.class);

    /** Format of the audio sound */
    public static final MetaKey<SoundFormat> FORMAT = make("format",
            SoundFormat.class);

    /** Author */
    public static final MetaKey<String> AUTHOR = make("author", String.class);
    
    /** Size of the file */
    public static final MetaKey<Long> FILE_SIZE = make("file_size", Long.class);

    /**
     * Creates new MetaKey object
     * 
     * @param name
     *            Name of the entry
     * @param type
     *            Type of the assoaciated data
     * @return New key
     */
    public static <T> MetaKey<T> make(String name, Class<T> type) {
        return new MetaKey<T>(name, type);
    }

}
