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

		private final Enumeration<URL> configFiles;

		private Iterator<ParserDefinition> definitions;

		private boolean foundNext = false;

		public ParserIterator() throws ParserDiscoveryException {
			try {
				this.configFiles = classLoader.getResources(configPath);
			} catch (IOException e) {
				throw new ParserDiscoveryException(e);
			}
		}

		@Override
		public boolean hasNext() {
			findNextIfNeeded();
			return foundNext;
		}

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

		private void findNextIfNeeded() {
			if (!foundNext) {
				try {
					foundNext = findNext();
				} catch (ParserDiscoveryException e) {
					throw new RuntimeException(e);
				}
			}
		}

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

		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"ParserIterator does not support removal");
		}

	}

}
