package pl.edu.agh.ki.grieg.decoder;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

/**
 * Class used to maintain collection of {@code AudioFormatParser}s and find
 * appropriate parsers.
 * 
 * @author los
 */
public class DecoderManager {

	private static final Logger logger = LoggerFactory
			.getLogger(DecoderManager.class);

	/**
	 * Parent manager, should be consulted if this manager cannot find suitable
	 * parser
	 */
	private final DecoderManager parent;

	/** mappipng: extension -> providers */
	private final Multimap<String, AudioFormatParser> decoders;

	/** set of all the decoders */
	private final Set<AudioFormatParser> decoderSet;

	/**
	 * Creates a new, empty decoder manager with specified decoder manager as
	 * the parent.
	 * 
	 * @param parent
	 *            Parent of the newly created decoder manager
	 */
	public DecoderManager(DecoderManager parent) {
		this.parent = parent;
		decoders = ArrayListMultimap.create();
		decoderSet = Sets.newHashSet();
		if (parent != null) {
			logger.debug("Created decoder manager with parent={}", parent);
		} else {
			logger.debug("Created top-level decoder manager");
		}
	}

	/**
	 * Creates a new, empty decoder manager
	 */
	public DecoderManager() {
		this(null);
	}

	/**
	 * Registers {@code AudioFormatParser} provider with a given extension.
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
	 * specified extensions.
	 * 
	 * @param parser
	 *            Provider to be registered
	 * @param extensions
	 *            Extensions to associate parser with
	 */
	public void register(AudioFormatParser parser, Iterable<String> extensions) {
		for (String ext : extensions) {
			register(ext, parser);
		}
		logger.debug("Registered format provider {} for {}", parser, extensions);
	}

	/**
	 * @return Unmodifiable set of all the registered decoders
	 */
	public Set<AudioFormatParser> getAllDecoders() {
		Set<AudioFormatParser> parsers = Sets.newHashSet(decoderSet);
		if (parent != null) {
			parsers.addAll(parent.getAllDecoders());
		}
		return Collections.unmodifiableSet(parsers);
	}

	/**
	 * @return Set of strings containing all the extensions with matching parser
	 *         entries
	 */
	public Set<String> getKnownExtensions() {
		Set<String> extensions = Sets.newHashSet(decoders.keySet());
		if (parent != null) {
			extensions.addAll(parent.getKnownExtensions());
		}
		return extensions;
	}

	/**
	 * @return Unmodifiable multimap of extensions and matching decoders
	 */
	public Multimap<String, AudioFormatParser> getParsersMap() {
		return Multimaps.unmodifiableMultimap(decoders);
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
		Set<AudioFormatParser> parsers = Sets.newHashSet(decoders.get(ext));
		if (parent != null) {
			Iterables.addAll(parsers, parent.getByExtension(ext));
		}
		return Collections.unmodifiableCollection(parsers);
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
