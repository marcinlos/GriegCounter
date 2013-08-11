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
     * Begins processing the audio data.
     * 
     * @throws AudioException
     *             If an audio-related exception occurs
     * @throws IOException
     *             If an IO error occurs
     */
    void start() throws AudioException, IOException;

    /**
     * Pauses data processing with the possibiliity to resume it later. If the
     * processing has already finished, it does nothing and returns
     * {@code false}.
     * 
     * @return {@code true} if the processing has been in progress when the
     *         method was called and it was trully paused, {@code false} if it
     *         had finished by then
     */
    boolean pause();

    /**
     * Resumes previously paused processing.
     */
    void resume();

    /**
     * Definitely stops processing, if it has not been finished already.
     * Otherwise, it does nothing.
     * 
     * @return {@code true} if the processing has been in progress when the
     *         method was called and it was trully paused, {@code false} if it
     *         had finished by then
     */
    boolean stop();

}