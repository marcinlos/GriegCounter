package pl.edu.agh.ki.grieg.decoder.discovery;

import static com.google.common.base.Preconditions.checkNotNull;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;

/**
 * Immutable structure consisting of audio format parser object and extensions
 * of commonly supported files.
 * 
 * @author los
 */
public final class ParserEntry {

	/** Parser object */
	private final AudioFormatParser parser;

	/** Extensions of supported file types */
	private final Iterable<String> extensions;

	/**
	 * Creates new {@link ParserEntry} holding specified parser instance, and
	 * the collection of extensions.
	 * 
	 * @param parser
	 *            Parser instance
	 * @param extensions
	 *            Extensions of supported files
	 */
	public ParserEntry(AudioFormatParser parser, Iterable<String> extensions) {
		this.parser = checkNotNull(parser);
		this.extensions = checkNotNull(extensions);
	}

	/**
	 * @return Parser instance
	 */
	public AudioFormatParser getParser() {
		return parser;
	}

	/**
	 * @return Sequence of extensions of supported file types
	 */
	public Iterable<String> getExtensions() {
		return extensions;
	}

}
