package pl.edu.agh.ki.grieg.decoder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;

import com.google.common.io.Files;

/**
 * Class used to maintain collection of {@code AudioFormatParser}s and find
 * appropriate parsers.
 * 
 * @author los
 */
public class DecoderManager {

    /** mappipng: extension -> providers */
    private Map<String, List<AudioFormatParser>> decoders;
    
    /** set of all the decoders */
    private Set<AudioFormatParser> decoderSet;

    /**
     * Creates a new, empty decoder manager
     */
    public DecoderManager() {
        decoders = new HashMap<String, List<AudioFormatParser>>();
        decoderSet = new HashSet<AudioFormatParser>();
    }

    /**
     * Registers {@code AudioFormatParser} provider with a given extension. Not
     * ment to be called externally, full information about supported types can
     * be obtained {@linkplain AudioFormatParser#extensions()}.
     * 
     * @param ext
     *            File extension to be associated with given provider
     * @param provider
     *            {@code AudioFormatParser} provider
     */
    private void register(String ext, AudioFormatParser provider) {
        List<AudioFormatParser> forExt = decoders.get(ext);
        if (forExt == null) {
            forExt = new ArrayList<AudioFormatParser>();
            decoders.put(ext, forExt);
        }
        forExt.add(provider);
        decoderSet.add(provider);
    }

    /**
     * Registers {@code AudioFormatParser} provider, associating it with all the
     * extensions returned by the {@linkplain AudioFormatParser#extensions()}
     * method.
     * 
     * @param provider
     *            Provider to be registered
     */
    public void register(AudioFormatParser provider) {
        for (String ext : provider.extensions()) {
            register(ext, provider);
        }
    }
    
    /**
     * @return Unmodifiable set of all the registered decoders
     */
    public Set<AudioFormatParser> getAllDecoders() {
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
    public Iterable<AudioFormatParser> getByExtension(String ext) {
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
    public Iterable<AudioFormatParser> getByExtension(File file) {
        String ext = Files.getFileExtension(file.getName());
        return getByExtension(ext);
    }

}
