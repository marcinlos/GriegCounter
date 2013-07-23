package pl.edu.agh.ki.grieg.decoder;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

/**
 * Class used to maintain collection of {@code AudioFormatParser}s and find
 * appropriate parsers.
 * 
 * @author los
 */
public class DecoderManager {

    /**
     * Parent manager, should be consulted if this manager cannot find suitable
     * parser
     */
    private DecoderManager parent;

    /** mappipng: extension -> providers */
    private Multimap<String, AudioFormatParser> decoders;

    /** set of all the decoders */
    private Set<AudioFormatParser> decoderSet;

    public DecoderManager(DecoderManager parent) {
        this.parent = parent;
        decoders = ArrayListMultimap.create();
        decoderSet = Sets.newHashSet();
    }

    /**
     * Creates a new, empty decoder manager
     */
    public DecoderManager() {
        this(null);
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
        decoders.put(ext, provider);
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
        if (parent != null) {
            Set<AudioFormatParser> set = Sets.newHashSet();
            set.addAll(decoderSet);
            set.addAll(parent.getAllDecoders());
            return set;
        } else {
            return Collections.unmodifiableSet(decoderSet);
        }
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
        if (parent != null) {
            Set<AudioFormatParser> parsers = Sets.newHashSet();
            Iterables.addAll(parsers, parent.getByExtension(ext));
            parsers.addAll(decoders.get(ext));
            return parsers;
        } else {
            return Collections.unmodifiableCollection(decoders.get(ext));
        }
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
