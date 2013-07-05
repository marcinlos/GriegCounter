package pl.edu.agh.ki.grieg.utils;

import java.io.FilterInputStream;
import java.io.InputStream;

public class BinaryInputStream extends FilterInputStream {

    public BinaryInputStream(InputStream in) {
        super(in);
    }
    
}
