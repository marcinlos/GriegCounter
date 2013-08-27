package pl.edu.agh.ki.grieg.util.classpath;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.common.collect.Sets;

/**
 * URL protocol handler capable of reading contents of jar files.
 * 
 * @author los
 */
public class JarProtocolHandler implements URLProtocolHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getFiles(String basedir, URL location)
            throws IOException {
        Set<String> files = Sets.newLinkedHashSet();
        JarFile jar = getJar(location);
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (matches(entry, basedir)) {
                files.add(entry.getName());
            }
        }
        return files;
    }

    /**
     * Checks if the specified entry is appropriate to be included in search
     * results:
     * <ul>
     * <li>it is in the appropriate directory
     * <li>it is not a directory
     * </ul>
     * 
     * @param entry
     *            Jar file entry to examine
     * @param basedir
     *            Directory in which we search for files
     * @return {@code true} if the entry meets criteria, {@code false} otherwise
     */
    private static boolean matches(JarEntry entry, String basedir) {
        String name = entry.getName();
        if (!name.startsWith(basedir)) {
            return false;
        }
        String tail = name.substring(basedir.length());
        if (tail.indexOf('/') != -1) {
            return false;
        }
        if (entry.isDirectory()) {
            return false;
        }
        return true;
    }

    /**
     * Obtains {@link JarFile} reference from specified jar URL.
     * 
     * @param url
     *            Jar URL
     * @return {@link JarFile} object
     * @throws IOException
     *             If an IO error occured
     */
    private static JarFile getJar(URL url) throws IOException {
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        return connection.getJarFile();
    }

}
