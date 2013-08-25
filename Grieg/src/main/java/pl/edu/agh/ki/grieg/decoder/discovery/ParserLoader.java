package pl.edu.agh.ki.grieg.decoder.discovery;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.ReflectionException;
import pl.edu.agh.ki.grieg.util.Resources;

/**
 * Provides a way to lazily iterate over all the discovered audio parsers.
 * During iteration searches the classpath for all the configuration files
 * containing audio parser definitions, and instantiates them parsers one by
 * one.
 * 
 * <p>
 * Configuration files are understood as these matching the specified
 * classpath-relative path (e.g. in case of multiple jars on the classpath there
 * may be many resources for each classpath-relative path), as found by the
 * specified classloader. Calling thread's context classloader is used by
 * default. Such files are also traversed lazily, i.e. file is parsed in full,
 * and the next one is not examined until all the definitions from the previous
 * one are returned by the iterator's {@link Iterator#next()} calls.
 * 
 * <p>
 * Due to lazy nature, both {@link Iterator#hasNext()} and
 * {@link Iterator#next()} may fail. Ideally, {@link ParserDiscoveryException}
 * would be thrown. Unfortunetely, since it is a checked exception, it is not
 * possible due to these methods' signatures. Exceptions occuring during the
 * lazy traversal and instantiation of parsers are wrapped in
 * {@link RuntimeException} and propagated outside.
 * 
 * @author los
 */
public class ParserLoader implements Iterable<ParserEntry> {

	/** Classpath-relative path of the traversed files */
	private final String configPath;

	/** Classloader used to find the files */
	private final ClassLoader classLoader;

	/** Parser used to parse the config files */
	private final ProviderFileParser parser;

	/**
	 * Creates new {@link ParserLoader} examining all the files matching
	 * {@code configPath} at the classpath, found with the speified classloader.
	 * 
	 * @param configPath
	 *            Path of the config files
	 * @param classLoader
	 *            Classloader used to find the config files
	 */
	public ParserLoader(String configPath, ClassLoader classLoader) {
		this.configPath = configPath;
		this.classLoader = classLoader;
		this.parser = new ProviderFileParser();
	}

	/**
	 * Creates new {@link ParserLoader} examining all the files matching
	 * {@code configPath} at the classpath, found using this thread's context
	 * classloader.
	 * 
	 * @param configPath
	 *            Path of the config files
	 */
	public ParserLoader(String configPath) {
		this(configPath, Resources.contextClassLoader());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Returns lazy iterator over parsers found in the configuration files.
	 */
	@Override
	public Iterator<ParserEntry> iterator() {
		try {
			return new ParserIterator();
		} catch (ParserDiscoveryException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Implementation of the lazy iterator.
	 */
	private final class ParserIterator implements Iterator<ParserEntry> {

		/** Sequence of URLs of found config files */
		private final Enumeration<URL> configFiles;

		/**
		 * Iterator over the parser definitions found in the last parsed file
		 */
		private Iterator<ParserDefinition> definitions;

		/**
		 * Whether the next definition has been found, needed since the
		 * {@link #hasNext()} needs to find it if it has not yet been found
		 */
		private boolean foundNext = false;

		/**
		 * Creates new lazy iterator.
		 * 
		 * @throws ParserDiscoveryException
		 *             If an IO error occured during searching for config files
		 */
		public ParserIterator() throws ParserDiscoveryException {
			try {
				this.configFiles = classLoader.getResources(configPath);
			} catch (IOException e) {
				throw new ParserDiscoveryException(e);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * <p>
		 * Determines the next definition if necessary.
		 */
		@Override
		public boolean hasNext() {
			findNextIfNeeded();
			return foundNext;
		}

		/**
		 * Determines the next parser definition, taking the next definition
		 * from the currently traversed file or moving to the next one if
		 * necessary.
		 * 
		 * @return {@code true} if the next definition exists, {@code false}
		 *         otherwise
		 * @throws ParserDiscoveryException
		 *             If processing the config file fails
		 */
		private boolean findNext() throws ParserDiscoveryException {
			if (definitions != null && definitions.hasNext()) {
				return true;
			} else {
				while (openNextFile()) {
					if (definitions.hasNext()) {
						return true;
					}
				}
				return false;
			}
		}

		/**
		 * Opens and parses the next config file found on the classpath.
		 * 
		 * @return {@code true} if there was another file, {@code false} if the
		 *         previous one was the last
		 * @throws ParserDiscoveryException
		 *             If an error occurs during file processing
		 */
		private boolean openNextFile() throws ParserDiscoveryException {
			if (!configFiles.hasMoreElements()) {
				return false;
			}
			URL url = configFiles.nextElement();
			try {
				InputStream stream = url.openStream();
				definitions = parser.parse(stream).iterator();
				return true;
			} catch (IOException e) {
				throw new ParserDiscoveryException(e);
			}
		}

		/**
		 * If the next definition has not been determined yet, it attempts to do
		 * it.
		 */
		private void findNextIfNeeded() {
			if (!foundNext) {
				try {
					foundNext = findNext();
				} catch (ParserDiscoveryException e) {
					throw new RuntimeException(e);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * <p>
		 * Instantiates the parser described by the next definition.
		 */
		@Override
		public ParserEntry next() {
			findNextIfNeeded();
			if (!foundNext) {
				throw new NoSuchElementException();
			} else {
				foundNext = false;
				return createNextEntry();
			}
		}

		/**
		 * Instantiates {@link AudioFormatParser} based on the next available
		 * definition.
		 */
		private ParserEntry createNextEntry() {
			ParserDefinition definition = definitions.next();
			String className = definition.getClassName();
			try {
				AudioFormatParser formatParser = Reflection.create(className);
				return new ParserEntry(formatParser, definition.getExtensions());
			} catch (ReflectionException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * Always throws {@link UnsupportedOperationException} for this iterator
		 * implementation.
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"ParserIterator does not support removal");
		}

	}

}
