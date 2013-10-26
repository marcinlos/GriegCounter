package pl.edu.agh.ki.grieg.decoder.discovery;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;

import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;
import pl.edu.agh.ki.grieg.util.Resources;
import pl.edu.agh.ki.grieg.util.classpath.ClasspathScanner;
import pl.edu.agh.ki.grieg.util.classpath.ResourceResolver;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ParserLoaderTest {

    private ParserLoader loader;

    @Test
    public void canLoadEmpty() throws Exception {
        loader = new ParserLoader("parsers/empty");
        Iterator<ParserEntry> it = loader.iterator();
        assertFalse(it.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cannotRemove() throws Exception {
        loader = new ParserLoader("parsers/one");
        Iterator<ParserEntry> it = loader.iterator();
        it.next();
        it.remove();
    }

    @Test
    public void canLoadOneFromOneFile() throws Exception {
        loader = new ParserLoader("parsers/one");
        Iterator<ParserEntry> it = loader.iterator();
        assertTrue(it.hasNext());
        ParserEntry entry = it.next();

        AudioFormatParser parser = entry.getParser();
        assertEquals(parser.getClass(), DummyAudio1.class);
        assertEquals(ImmutableSet.of("mp3", "ogg"), entry.getExtensions());
        assertFalse(it.hasNext());
    }

    @Test
    public void canLoadMultipleFromOneFile() throws Exception {
        loader = new ParserLoader("parsers/two");
        Set<ParserEntry> actual = Sets.newHashSet(loader);
        Set<Class<?>> classes = getClasses(actual);
        assertEquals(ImmutableSet.of(DummyAudio2.class, DummyAudio3.class),
                classes);
    }

    @Test
    public void canLoadFromMultipleFiles() throws Exception {
        Iterator<URL> urls = asURLs("parsers/one", "parsers/empty",
                "parsers/two");
        ResourceResolver resolver = mock(ResourceResolver.class);
        when(resolver.getResources(anyString())).thenReturn(urls);
        ClasspathScanner scanner = mock(ClasspathScanner.class);
        when(scanner.getResolver()).thenReturn(resolver);
        when(scanner.getEntries(anyString()))
                .thenReturn(ImmutableSet.of("config"));

        loader = new ParserLoader("parsers", scanner);

        Set<Class<?>> classes = getClasses(loader);
        assertEquals(ImmutableSet.of(DummyAudio1.class, DummyAudio2.class,
                DummyAudio3.class), classes);
    }

    @Test(expected = NoSuchElementException.class)
    public void nextPastTheEndCausesException() throws Exception {
        loader = new ParserLoader("parsers/one");
        Iterator<ParserEntry> it = loader.iterator();
        it.next();
        it.next();
    }

    @Test(expected = ParserDiscoveryException.class)
    public void cannotLoadInvalid() throws Throwable {
        loader = new ParserLoader("parsers/invalid");
        try {
            loader.iterator().next();
        } catch (RuntimeException e) {
            throw e.getCause();
        }
    }

    private Set<Class<?>> getClasses(Iterable<ParserEntry> entries) {
        Set<Class<?>> classes = Sets.newHashSet();
        for (ParserEntry entry : entries) {
            classes.add(entry.getParser().getClass());
        }
        return classes;
    }

    private static Iterator<URL> asURLs(String... paths) throws Exception {
        List<URL> urls = Lists.newArrayList();
        for (String path : paths) {
            urls.add(Resources.get(path));
        }
        return urls.iterator();
    }

}
