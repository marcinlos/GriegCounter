package pl.edu.agh.ki.grieg.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Streams {

    private Streams() {
        // non-instantiable
    }
    
    public static void close(InputStream stream) throws IOException {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }
    
    public static void close(OutputStream stream) throws IOException {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }

}
