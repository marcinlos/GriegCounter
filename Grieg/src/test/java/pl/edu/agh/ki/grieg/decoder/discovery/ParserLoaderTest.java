package pl.edu.agh.ki.grieg.decoder.discovery;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;

import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;

import com.google.common.collect.ImmutableSet;
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
        Set<Class<?>> classes = Sets.newHashSet();
        for (ParserEntry entry : actual) {
            classes.add(entry.getParser().getClass());
        }

        assertEquals(ImmutableSet.of(DummyAudio2.class, DummyAudio3.class),
                classes);
    }

    @Test(expected = NoSuchElementException.class)
    public void nextCausesException() throws Exception {
        loader = new ParserLoader("parsers/one");
        Iterator<ParserEntry> it = loader.iterator();
        it.next();
        it.next();
    }

}
