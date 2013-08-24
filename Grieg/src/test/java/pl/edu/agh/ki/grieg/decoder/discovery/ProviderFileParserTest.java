package pl.edu.agh.ki.grieg.decoder.discovery;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class ProviderFileParserTest {

    private ProviderFileParser parser;

    @Before
    public void setup() {
        parser = new ProviderFileParser();
    }

    @Test
    public void canParseEmptyFile() throws Exception {
        assertThat(parser.parse(""), empty());
    }

    @Test
    public void canParseSingleEntryWithOneExtension() throws Exception {
        String content = "@class: pl.edu.agh.ki.grieg.mp3.Parser\n" +
                "@extensions: mp3";

        Set<ParserDefinition> actual = parser.parse(content);
        ParserDefinition definition = new ParserDefinition(
                "pl.edu.agh.ki.grieg.mp3.Parser",
                ImmutableSet.of("mp3"));

        assertEquals(ImmutableSet.of(definition), actual);
    }

    @Test
    public void canParseSingleEntryWithMultipleExtensions() throws Exception {
        String content = "@class: pl.edu.agh.ki.grieg.mp3.Parser\n" +
                "@extensions: mp3 wav ogg";

        Set<ParserDefinition> actual = parser.parse(content);
        ParserDefinition definition = new ParserDefinition(
                "pl.edu.agh.ki.grieg.mp3.Parser",
                ImmutableSet.of("mp3", "wav", "ogg"));

        assertEquals(ImmutableSet.of(definition), actual);
    }
    
    @Test(expected = SyntaxException.class)
    public void cannotParseWithNoAtSignAtClass() throws Exception {
        String content = "class: pl.edu.agh.ki.grieg.mp3.Parser\n" +
                "@extensions: mp3 wav ogg";
        parser.parse(content);
    }
    
    @Test(expected = SyntaxException.class)
    public void cannotParseWithNoAtSignAtExtensions() throws Exception {
        String content = "@class: pl.edu.agh.ki.grieg.mp3.Parser\n" +
                "extensions: mp3 wav ogg";
        parser.parse(content);
    }

    @Test
    public void cannotParseInvalidClassKeyword() throws Exception {
        try {
            String content = "@clasgfd: pl.edu.agh.ki.grieg.mp3.Parser\n" +
                    "@extensions: mp3 wav ogg";
            Set<ParserDefinition> parsers = parser.parse(content);
            fail("Exception should have been thrown, but got " + parsers);
        } catch (SyntaxException e) {
            assertEquals("\"@class\"", e.getExpected());
        } catch (Exception e) {
            fail("Expected SyntaxException, got: " + e);
        }
    }

    @Test
    public void cannotParseInvalidExtensionsKeyword() throws Exception {
        try {
            String content = "@class: pl.edu.agh.ki.grieg.mp3.Parser\n" +
                    "@extesdfsons: mp3 wav ogg";
            Set<ParserDefinition> parsers = parser.parse(content);
            fail("Exception should have been thrown, but got " + parsers);
        } catch (SyntaxException e) {
            assertEquals("\"@extensions\"", e.getExpected());
        } catch (Exception e) {
            fail("Expected SyntaxException, got: " + e);
        }
    }
    
    @Test
    public void cannotParseInvalidClassName() throws Exception {
        try {
            String content = "@class: .3grieg.mp3.Parser\n" +
                    "@extensions: mp3 wav ogg";
            Set<ParserDefinition> parsers = parser.parse(content);
            fail("Exception should have been thrown, but got " + parsers);
        } catch (SyntaxException e) {
            assertEquals("class name", e.getExpected());
        } catch (Exception e) {
            fail("Expected SyntaxException, got: " + e);
        }
    }
    
    @Test
    public void canParseMultipleDefinitions() throws Exception {
        String content = "@class: pl.edu.agh.ki.grieg.mp3.Parser\n" +
                "@extensions: mp3 wav\n\n" +
                "@class: pl.edu.agh.ki.grieg.wav.Parser\n" +
                "@extensions: wav";
        
        Set<ParserDefinition> actual = parser.parse(content);
        ParserDefinition mp3 = new ParserDefinition(
                "pl.edu.agh.ki.grieg.mp3.Parser",
                ImmutableSet.of("mp3", "wav"));
        
        ParserDefinition wav = new ParserDefinition(
                "pl.edu.agh.ki.grieg.wav.Parser",
                ImmutableSet.of("wav"));

        assertEquals(ImmutableSet.of(mp3, wav), actual);
    }

}
