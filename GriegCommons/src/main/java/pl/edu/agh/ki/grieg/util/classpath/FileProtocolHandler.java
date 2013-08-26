package pl.edu.agh.ki.grieg.util.classpath;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

import com.google.common.collect.Sets;

public class FileProtocolHandler implements URLProtocolHandler {

    @Override
    public Set<String> getFiles(String basedir, URL url) {
        Set<String> files = Sets.newLinkedHashSet();
        File file = new File(url.getPath());
        if (basedir.endsWith("/")) {
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    if (child.isFile()) {
                        files.add(basedir + child.getName());
                    }
                }
            }
        } else {
            if (file.exists()) {
                files.add(basedir);
            }
        }
        return files;
    }

    @Override
    public Set<String> getFilesRecursively(String basedir, URL location)
            throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
