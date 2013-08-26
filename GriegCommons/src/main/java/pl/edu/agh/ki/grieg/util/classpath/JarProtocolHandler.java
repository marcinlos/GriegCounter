package pl.edu.agh.ki.grieg.util.classpath;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.common.collect.Sets;

public class JarProtocolHandler implements URLProtocolHandler {

    @Override
    public Set<String> getFiles(String basedir, URL location)
            throws IOException {
        Set<String> files = Sets.newLinkedHashSet();
        JarURLConnection connection = (JarURLConnection) location.openConnection();
        JarFile jar = connection.getJarFile();
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.startsWith(basedir)) {
                String tail = name.substring(basedir.length());
                if (tail.indexOf('/') == -1) {
                    if (!entry.isDirectory()) {
                        files.add(name);
                    }
                }
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
