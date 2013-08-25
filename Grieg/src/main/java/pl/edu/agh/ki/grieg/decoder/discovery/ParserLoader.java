package pl.edu.agh.ki.grieg.decoder.discovery;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.ReflectionException;
import pl.edu.agh.ki.grieg.util.Resources;

public class ParserLoader implements Iterable<ParserEntry> {

    private static final Logger logger = LoggerFactory
            .getLogger(ParserLoader.class);

    private final String configPath;

    private final ClassLoader classLoader;

    private final ProviderFileParser parser;

    public ParserLoader(String configPath) {
        this.configPath = configPath;
        this.classLoader = Resources.contextClassLoader();
        this.parser = new ProviderFileParser();
    }

    @Override
    public Iterator<ParserEntry> iterator() {
        try {
            return new ParserIterator();
        } catch (ParserDiscoveryException e) {
            throw new RuntimeException(e);
        }
    }

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
            InputStream stream;
            try {
                stream = url.openStream();
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
                    "ParserIterator does not allow removal");
        }

    }

}
