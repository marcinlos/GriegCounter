package pl.edu.agh.ki.grieg.decoder.discovery;

import java.util.List;

import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;

public final class ParserEntry {
    
    private final AudioFormatParser parser;
    
    private final List<String> extensions;

    public ParserEntry(AudioFormatParser parser, List<String> extensions) {
        this.parser = parser;
        this.extensions = extensions;
    }
    
    public AudioFormatParser getParser() {
        return parser;
    }
    
    public Iterable<String> getExtensions() {
        return extensions;
    }

}
