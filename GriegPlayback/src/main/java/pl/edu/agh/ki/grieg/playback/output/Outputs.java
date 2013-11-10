package pl.edu.agh.ki.grieg.playback.output;

import java.util.ServiceLoader;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.output.spi.OutputFactory;
import pl.edu.agh.ki.grieg.playback.PlaybackException;

/**
 * Static methods for dealing with audio outputs and its factories.
 * 
 * @author los
 */
public final class Outputs {

    /** Service loader */
    private static final ServiceLoader<OutputFactory> loader;
    
    /** Available {@link OutputFactory} implementations */
    private static Set<OutputFactory> factories = Sets.newHashSet(); 
    
    static {
        loader = ServiceLoader.load(OutputFactory.class);
        Iterables.addAll(factories, loader);
    }

    private Outputs() {
        // non-instantiable
    }

    /**
     * @return Default audio output factory
     */
    public static OutputFactory getFactory() {
        if (! factories.isEmpty()) {
            return Iterables.getFirst(factories, null);
        } else {
            throw new Error("No factory provider");
        }
    }

    /**
     * Creates new {@link AudioOutput} for specified format using default
     * factory.
     * 
     * @see OutputFactory#newOutput(SoundFormat)
     */
    public static AudioOutput newOutput(SoundFormat format)
            throws PlaybackException {
        return getFactory().newOutput(format);
    }

    /**
     * Creates audio output bound to specified data source using specified
     * factory. Output is started and ready to play.
     * 
     * @param factory
     *            Factory to create outptu with
     * @param source
     *            Audio data source
     * @return {@link AudioOutput} bound to specified data source
     * @throws PlaybackException
     *             If the output cannot be created
     */
    public static AudioOutput boundTo(OutputFactory factory,
            SampleEnumerator source) throws PlaybackException {
        AudioOutput output = factory.newOutput(source.getFormat());
        source.connect(output);
        output.start();
        return output;
    }

    /**
     * Creates audio output bound to specified data source using default
     * factory. Output is started and ready to play.
     * 
     * @param source
     *            Audio data source
     * @return {@link AudioOutput} bound to specified data source
     * @throws PlaybackException
     *             If the output cannot be created
     */
    public static AudioOutput boundTo(SampleEnumerator source)
            throws PlaybackException {
        return boundTo(getFactory(), source);
    }

}
