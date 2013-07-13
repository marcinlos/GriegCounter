package pl.edu.agh.ki.grieg.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BinaryInputStream extends FilterInputStream {

    public BinaryInputStream(InputStream in) {
        super(in);
    }
    
    public int readIntBE() throws IOException {
        return in.read() << 24 | in.read() << 16 | in.read() << 8 | in.read();
    }
    
    public long readUnsignedBE() throws IOException {
        return ((long) readIntBE()) & 0xffffffff;
    }
    
    public int readIntLE() throws IOException {
        return in.read() | in.read() << 8 | in.read() << 16 | in.read() << 24;
    }
    
    public long readUnsignedLE() throws IOException {
        return ((long) readIntLE()) & 0xffffffff;
    }
    
    public short readShortBE() throws IOException {
        return (short) (in.read() << 8 | in.read());
    }
    
    public int readUnsignedShortBE() throws IOException {
        return ((int) readShortBE()) & 0xffff;
    }
    
    public short readShortLE() throws IOException {
        return (short) (in.read() | in.read() << 8);
    }
    
    public int readUnsignedShortLE() throws IOException {
        return ((int) readShortLE()) & 0xffff;
    }
    
}
