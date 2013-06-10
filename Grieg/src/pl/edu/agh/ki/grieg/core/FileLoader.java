package pl.edu.agh.ki.grieg.core;

import java.io.File;
import java.io.IOException;
import java.util.ServiceLoader;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.DecoderManager;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFileParser;
import pl.edu.agh.ki.grieg.io.AudioFile;

/**
 * 
 * 
 * @author los
 */
public class FileLoader {
    
    private DecoderManager decoders = new DecoderManager();
    
    private FileLoader instance = new FileLoader();
    
    private FileLoader() {
        registerBuiltins();
        
        // gather all the implementations
        for (AudioFileParser p: ServiceLoader.load(AudioFileParser.class)) {
            decoders.register(p);
        }
    }
    
    
    private void registerBuiltins() {
        // register builtin decoders here
    }
    
    /**
     * @return Instance of {@code FileLoader}
     */
    public FileLoader getInstance() {
        return instance;
    }
    
    /**
     * 
     * @param file File containing audio data to load
     * @return {@code AudioFile}
     * @throws NoSuitableDecoderException If no decoder capable of decoding this file has been found
     * @throws DecodeException If some error occured during decoding
     * @throws IOException If plain IO error occured
     */
    public AudioFile loadFile(File file) throws DecodeException, IOException {
        return null;
    }

}
