package pl.edu.agh.ki.grieg.gui.swing;

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

public class Settings {

    private String directory;
    
    private List<String> files = Lists.newArrayList();
    
    public Settings(File directory) {
        this(directory.getPath());
    }
    
    public Settings(String path) {
        this.directory = path;
    }
    
    public String getDirectory() {
        return directory;
    }
    
    public void setDirectory(String path) {
        this.directory = path;
    }
    
    public void setDirectory(File directory) {
        setDirectory(directory.getPath());
    }
    
    public List<String> getFiles() {
        return Collections.unmodifiableList(files);
    }
    
    public void addFile(String path) {
        files.add(path);
    }

    public void addFile(File file) {
        files.add(file.getPath());
    }
    
}
