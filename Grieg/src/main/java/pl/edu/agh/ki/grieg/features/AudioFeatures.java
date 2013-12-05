package pl.edu.agh.ki.grieg.features;

import static pl.edu.agh.ki.grieg.util.properties.Keys.make;
import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.util.properties.Key;

/**
 * Container for static {@link Key}s of commonly used audio file properties.
 * 
 * @author los
 */
public final class AudioFeatures {

    private AudioFeatures() {
        // non-instantiable
    }

    /** Number of PCM samples in the whole audio file */
    public static final Key<Long> SAMPLES = make("samples", long.class);

    /** Guess at number of samples in the whole audio file */
    public static final Key<Long> APPROX_SAMPLES = make("approx_samples",
            Long.class);

    /** Duration (in seconds) of the audio */
    public static final Key<Float> DURATION = make("duration", float.class);

    /** Format of the audio sound */
    public static final Key<SoundFormat> FORMAT = make("format",
            SoundFormat.class);
    
    /** Bitrate */
    public static final Key<Integer> BITRATE = make("bitrate", int.class);
    
    /** Encoding - e.g. mp3 */
    public static final Key<String> ENCODING = make("encoding", String.class);
    
    /** Depth */
    public static final Key<Integer> DEPTH = make("depth", int.class);
    
    /** Is VBR? */
    public static final Key<Boolean> VBR = make("vbr", boolean.class);

    /** Author */
    public static final Key<String> AUTHOR = make("author", String.class);
    
    /** Title */
    public static final Key<String> TITLE = make("title", String.class);
    
    /** Album */
    public static final Key<String> ALBUM = make("album", String.class);
    
    /** Genre */
    public static final Key<String> GENRE = make("genre", String.class);
    
    /** Track */
    public static final Key<Integer> TRACK = make("track", int.class);
    
    /** Track total */
    public static final Key<Integer> TRACK_TOTAL = make("track_total", int.class);
    
    /** Disc */
    public static final Key<Integer> DISC = make("disc", int.class);
    
    /** Disc total */
    public static final Key<Integer> DISC_TOTAL = make("disc_total", int.class);
    
    /** Year */
    public static final Key<Integer> YEAR = make("year", int.class);

    /** Size of the file */
    public static final Key<Long> FILE_SIZE = make("file_size", long.class);

}
