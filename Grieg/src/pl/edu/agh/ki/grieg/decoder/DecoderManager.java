package pl.edu.agh.ki.grieg.decoder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.ki.grieg.decoder.spi.AudioFileParser;
import pl.edu.agh.ki.grieg.utils.FileUtils;

/**
 * Class used to maintain collection of {@code AudioFileParser}s and find
 * appropriate parsers.
 * 
 * @author los
 */
class DecoderManager {

    /** mappipng: extension -> providers */
    private Map<String, List<AudioFileParser>> decoders;
    
    /** set of all the decoders */
    private Set<AudioFileParser> decoderSet;

    /**
     * Creates a new, empty decoder manager
     */
    public DecoderManager() {
        decoders = new HashMap<String, List<AudioFileParser>>();
        decoderSet = new HashSet<AudioFileParser>();
    }

    /**
     * Registers {@code AudioFileParser} provider with a given extension. Not
     * ment to be called externally, full information about supported types can
     * be obtained {@linkplain AudioFileParser#extensions()}.
     * 
     * @param ext
     *            File extension to be associated with given provider
     * @param provider
     *            {@code AudioFileParser} provider
     */
    private void register(String ext, AudioFileParser provider) {
        List<AudioFileParser> forExt = decoders.get(ext);
        if (forExt == null) {
            forExt = new ArrayList<AudioFileParser>();
            decoders.put(ext, forExt);
        }
        forExt.add(provider);
        decoderSet.add(provider);
    }

    /**
     * Registers {@code AudioFileParser} provider, associating it with all the
     * extensions returned by the {@linkplain AudioFileParser#extensions()}
     * method.
     * 
     * @param provider
     *            Provider to be registered
     */
    public void register(AudioFileParser provider) {
        for (String ext : provider.extensions()) {
            register(ext, provider);
        }
    }
    
    /**
     * @return Unmodifiable set of all the registered decoders
     */
    public Set<AudioFileParser> getAllDecoders() {
        return Collections.unmodifiableSet(decoderSet);
    }

    /**
     * Returns a sequence of parsers declaring themselves as capable of parsing
     * file formats often used with {@code ext} as extension.
     * 
     * @param ext
     *            File extension
     * @return Sequence of potentially compatible parsers
     * @see #getByExtension(File)
     */
    public Iterable<AudioFileParser> getByExtension(String ext) {
        return Collections.unmodifiableCollection(decoders.get(ext));
    }

    /**
     * Returns a sequence of parsers declaring potentially being capable of
     * parsing file formats often used with {@code ext} as extension.
     * 
     * @param file
     *            File to search for parsers for
     * @return Sequence of potentially compatible parsers
     * @see #getByExtension(String)
     */
    public Iterable<AudioFileParser> getByExtension(File file) {
        String ext = FileUtils.getExtension(file);
        return getByExtension(ext);
    }

}
