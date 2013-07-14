package pl.edu.agh.ki.grieg.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;

/**
 * Simple representation of an audio file. Contains information about the audio
 * source details, and an {@linkplain AudioStream} to retrieve. 
 * 
 * @author los
 */
public class AudioFile {

    private final File file;
    private final AudioFormatParser parser;
    
    public AudioFile(File file, AudioFormatParser parser) {
        this.file = file;
        this.parser = parser;
    }

    public File getFile() {
        return file;
    }

    public AudioFormatParser getParser() {
        return parser;
    }
    
    public AudioStream openStream() throws IOException, DecodeException {
        return parser.openStream(new FileInputStream(file));
    }
    

}
