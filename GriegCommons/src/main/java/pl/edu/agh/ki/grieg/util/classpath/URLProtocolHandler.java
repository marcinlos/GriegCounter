package pl.edu.agh.ki.grieg.util.classpath;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

/**
 * Interface of URL protocol implementations, providing traversal functionality
 * for various resources defined by URL.
 * 
 * @author los
 */
public interface URLProtocolHandler {

    /**
     * Determines names of all the regular files in the specified directory. It
     * does not perform reursive search, only {@code basedir} is ever
     * considered.
     * 
     * @param basedir
     *            Directory in which to search configuration
     * @param location
     *            Physical location of the file/directory to scan
     * @return Sequence of names of files in the specified directory
     * @throws IOException
     *             If there is an IO error during traversing directory
     * @see #getFilesRecursively(String, URL)
     */
    Set<String> getFiles(String basedir, URL location) throws IOException;

//    /**
//     * Determines names of all the regular files in the specified directory, as
//     * well as in all of its subdirectories.
//     * 
//     * @param basedir
//     *            Directory in which to start this.. configuration.
//     * @param location
//     *            Physical location of the file/directory to scan
//     * @return Sequence of names of files in the specified directory
//     * @throws IOException
//     *             If there is an IO error during traversing directory
//     * @see #getFiles(String, URL)
//     */
//    Set<String> getFilesRecursively(String basedir, URL location)
//            throws IOException;

}
