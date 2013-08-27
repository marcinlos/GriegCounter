package pl.edu.agh.ki.grieg.util.classpath;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.ReflectionException;
import pl.edu.agh.ki.grieg.util.Resources;

/**
 * Default {@link ProtocolHandlerProvider} implementation. Searches handler
 * configuration on the classpath in
 * {@code pl.edu.agh.ki.grieg/classpath.scanner/handlers/} directory. Given
 * protocol {@code prot}, it attempts to open
 * {@code pl.edu.agh.ki.grieg/classpath.scanner/handlers/prot} and, upon
 * success, reads the whole content and interprets it as the name of the class
 * that implements {@link ProtocolHandlerProvider} and supports this particular
 * protocol. Such class is instantiated using the default (parameterless)
 * constructor.
 * 
 * @author los
 */
public class DefaultProtocolHandlerProvider implements ProtocolHandlerProvider {

    private static final Logger logger = LoggerFactory
            .getLogger(DefaultProtocolHandlerProvider.class);

    /** Location of the configuration files */
    private static final String PROVIDER_DIR =
            "pl.edu.agh.ki.grieg/classpath.scanner/handlers/";

    /**
     * {@inheritDoc}
     */
    @Override
    public URLProtocolHandler getHandler(String protocol)
            throws ClasspathException {
        InputStream input = openProtocolConfig(protocol);
        try {
            return input == null ? null : createHandler(input);
        } finally {
            closeStream(input);
        }
    }

    /**
     * Reads the class name from the configuration file and reflectively creates
     * its instance.
     * 
     * @param input
     *            Input configuration stream
     * @return Instance of appropriate handler
     * @throws ClasspathException
     *             If some error occurs in the process
     */
    private static URLProtocolHandler createHandler(InputStream input)
            throws ClasspathException {
        try {
            String className = readAsString(input);
            Object handler = Reflection.create(className);
            return (URLProtocolHandler) handler;
        } catch (IOException e) {
            throw new ClasspathException(e);
        } catch (ReflectionException e) {
            throw new ClasspathException(e);
        }
    }

    /**
     * Opens stream for reading the configuration file for specified protocol.
     * If there is no such file, returns {@code null}.
     * 
     * @param protocol
     *            Protocol to open config for
     * @return Stream for reading the appropriate configuration file, or
     *         {@code null}
     */
    private static InputStream openProtocolConfig(String protocol) {
        String path = PROVIDER_DIR + protocol;
        return Resources.asStream(path);
    }

    /**
     * Closes the stream and logs potential exception.
     * 
     * @param input
     *            Stream to close
     */
    private static void closeStream(InputStream input) {
        try {
            Closeables.close(input, true);
        } catch (IOException e) {
            logger.warn("Error during resource closing: ", e);
        }
    }

    /**
     * Reads contents of the stream and returns it as a {@link String}.
     * 
     * @param stream
     *            Stream to read from
     * @return String containing the whole file
     * @throws IOException
     *             If an IO error occurs
     */
    private static String readAsString(InputStream stream) throws IOException {
        Readable readable = new InputStreamReader(stream, Charsets.UTF_8);
        return CharStreams.toString(readable);
    }

}
