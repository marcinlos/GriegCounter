package pl.edu.agh.ki.grieg.utils;

import java.io.File;

/**
 * Auxilary file-related functionality.
 * 
 * @author los
 */
public class FileUtils {

    private FileUtils() {
        // non-instantiable
    }

    /**
     * Extracts file extension, i.e. longest postfix of the path not without a
     * dot.
     * 
     * @param file
     *            File to extract extension from
     * @return File extension
     */
    public static String getExtension(File file) {
        String name = file.getName();
        int idx = name.lastIndexOf('.');
        return name.substring(idx + 1);
    }

}
