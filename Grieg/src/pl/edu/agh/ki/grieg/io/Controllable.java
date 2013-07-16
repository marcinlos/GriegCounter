package pl.edu.agh.ki.grieg.io;

import java.io.IOException;

/**
 * Interface of an abstract sound generator. It provides methods allowing to
 * controll the 'playback' process.
 * 
 * @author los
 */
public interface Controllable {

    /**
     * Begins processing audio data
     * 
     * @throws AudioException
     *             If an audio-related exception occurs
     * @throws IOException
     *             If an IO error occurs
     */
    void start() throws AudioException, IOException;

    /**
     * Pauses data processing with the possibiliity to resume it later
     */
    void pause();

    /**
     * Resumes previously paused processing
     */
    void resume();

    /**
     * Definitely stops processing
     */
    void stop();

}