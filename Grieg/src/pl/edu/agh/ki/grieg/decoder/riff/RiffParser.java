package pl.edu.agh.ki.grieg.decoder.riff;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.util.Bytes;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInts;

/**
 * Parser of RIFF files. It is a very simple class, it does not support full
 * RIFF file semantics, as its' primary goal is to facilitate reading WAV files,
 * which are usually contained in the RIFF.
 * 
 * @author los
 */
public class RiffParser {

    /** "RIFF" bytes ("\x52\x49\x46\x46") */
    private static final int ASCII_RIFF = 0x52494646;

    /** Input stream */
    private InputStream input;

    /** Data stream interpreting input as big-endian */
    private DataInput bigEndian;

    /** Data stream interpreting input as little-endian */
    private DataInput littleEndian;

    public RiffParser(InputStream stream) {
        this.input = stream;
        this.bigEndian = new DataInputStream(stream);
        this.littleEndian = new LittleEndianDataInputStream(stream);
    }

    /**
     * Extracts the RIFF file header from the beginning of the file. Should be
     * called as the first method after the constructor, as it cannot "rewind"
     * to the beginning by itself.
     * 
     * @return RIFF file header
     * @throws NotRiffException
     *             If the stream does not represent RIFF file
     * @throws IOException
     *             If an IO error occurs
     */
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

    /**
     * Reads next chunk header. Should be called only after the previous chunk
     * is completely consumed, as it does not keep track of stream position.
     * 
     * @return Chunk header
     * @throws IOException
     *             If IO error occurs
     */
    public ChunkHeader readChunkHeader() throws IOException {
        ChunkHeader header = new ChunkHeader();
        header.setId(bigEndian.readInt());
        header.setSize(UnsignedInts.toLong(littleEndian.readInt()));
        return header;
    }

    /**
     * @return Input stream of the parser
     */
    public InputStream getStream() {
        return input;
    }

}
