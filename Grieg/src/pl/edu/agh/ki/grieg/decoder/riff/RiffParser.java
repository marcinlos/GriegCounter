package pl.edu.agh.ki.grieg.decoder.riff;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.utils.Bytes;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInts;

public class RiffParser {

    private static final int ASCII_RIFF = 0x52494646;

    private InputStream input;
    private DataInput bigEndian;
    private DataInput littleEndian;

    public RiffParser(InputStream stream) {
        this.input = stream;
        this.bigEndian = new DataInputStream(stream);
        this.littleEndian = new LittleEndianDataInputStream(stream);
    }

    public RiffHeader readRiffHeader() throws NotRiffException, IOException {
        RiffHeader header = new RiffHeader();
        int riff = bigEndian.readInt();
        if (riff != ASCII_RIFF) {
            throw new NotRiffException("Invalid main chunk ID: "
                    + Bytes.intToStringBE(riff));
        }
        header.setId(riff);
        header.setSize(UnsignedInts.toLong(littleEndian.readInt()));
        header.setFormat(bigEndian.readInt());
        return header;
    }

    public ChunkHeader readChunkHeader() throws IOException {
        ChunkHeader header = new ChunkHeader();
        header.setId(bigEndian.readInt());
        header.setSize(UnsignedInts.toLong(littleEndian.readInt()));
        return header;
    }
    
    public InputStream getStream() {
        return input;
    }

}
