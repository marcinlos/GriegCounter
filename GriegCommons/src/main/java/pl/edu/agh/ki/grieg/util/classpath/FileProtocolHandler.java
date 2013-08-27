package pl.edu.agh.ki.grieg.util.classpath;

import java.io.File;
import java.net.URL;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * URL protocol handler dealing with {@code file:} protocol.
 * 
 * @author los
 */
public class FileProtocolHandler implements URLProtocolHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getFiles(String basedir, URL url) {
        Set<String> files = Sets.newLinkedHashSet();
        File file = new File(url.getPath());
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                if (child.isFile()) {
                    files.add(basedir + child.getName());
                }
            }
        }
        return files;
    }

}
