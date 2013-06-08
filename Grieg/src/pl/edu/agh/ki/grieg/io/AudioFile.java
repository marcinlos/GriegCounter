package pl.edu.agh.ki.grieg.io;

import pl.edu.agh.ki.grieg.data.SourceDetails;

/**
 * Simple representation of an audio file. Contains information about the audio
 * source details, and an {@linkplain AudioStream} to retrieve. 
 * 
 * @author los
 */
public class AudioFile {

    private SourceDetails details;
    private AudioStream stream;
    
    
    public AudioFile(SourceDetails details, AudioStream stream) {
        this.details = details;
        this.stream = stream;
    }
    
    public SourceDetails getDetails() {
        return details;
    }
    
    public AudioStream getStream() {
        return stream;
    }

}
