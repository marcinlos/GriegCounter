package pl.edu.agh.ki.grieg.decoder.builtin.wav;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import pl.edu.agh.ki.grieg.data.Format;
import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.InvalidFormatException;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFileParser;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.meta.SimpleTagContainer;
import pl.edu.agh.ki.grieg.meta.TagSet;
import pl.edu.agh.ki.grieg.utils.Bytes;

public class WavFileParser implements AudioFileParser {

    private static final Iterable<String> EXTS = Arrays.asList("wav");

    private static final int ASCII_RIFF = 0x52494646;
    private static final int ASCII_WAVE = 0x57415645;
    private static final int ASCII_FMT = 0x666d7420;
    private static final int ASCII_DATA = 0x64617461;

    private static final int FMT_PCM = 1;

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<String> extensions() {
        return EXTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceDetails getDetails(InputStream stream) throws DecodeException,
            IOException {
        stream.mark(44);
        SourceDetails details = readDetails(new DataInputStream(stream));
        stream.reset();
        return details;
    }

    
    private SourceDetails readDetails(DataInputStream stream)
            throws InvalidFormatException, IOException {
        RiffHeader header = readHeader(stream);
        int size = header.getSubchunk2Size();
        int channels = header.getNumChannels();
        int bitsPerSample = header.getBitsPerSample();
        int sampleCount = size * channels * bitsPerSample / 8;
        int sampleRate = header.getSampleRate();
        float length = sampleCount / (float) sampleRate;
        Format format = new Format(channels, bitsPerSample, sampleRate);
        TagSet tags = new SimpleTagContainer();
        return new SourceDetails(null, length, sampleCount, format, tags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AudioFile open(InputStream stream) throws DecodeException,
            IOException {
        DataInputStream dataInput = new DataInputStream(stream);
        SourceDetails details = readDetails(dataInput);
        AudioStream audioStream = new WavStream(dataInput, details);
        return new AudioFile(details, audioStream);
    }

    /**
     * Helper method, extracting short and treating it as an unsigned value,
     * casting to integer.
     * 
     * @param stream
     *            Stream to extract value from
     * @return Integer representing value of unsigned short extracted from
     *         stream
     * @throws IOException
     *             If the stream read operation fails
     */
    private int readShort(DataInputStream stream) throws IOException {
        return 0xffff & stream.readShort();
    }

    /**
     * Reads a standard RIFF chunk header from the input stream.
     * 
     * @param stream
     *            Stream used as the source of data
     * @return Parsed RIFF header
     * @throws InvalidFormatException
     *             If the data in the stream is not a valid RIFF header
     * @throws IOException
     *             If ordinary IO error occurs during reading the stream
     */
    private RiffHeader readHeader(DataInputStream stream)
            throws InvalidFormatException, IOException {
        // TODO: this is wrong
        RiffHeader header = new RiffHeader();
        int chunkId = stream.readInt();
        if (chunkId != ASCII_RIFF) {
            throw new NotRiffException();
        }
        header.setChunkId(chunkId);
        header.setChunkSize(Bytes.toBE(stream.readInt()));
        int format = stream.readInt();
        if (format != ASCII_WAVE) {
            throw new NotWavException();
        }
        header.setFormat(format);
        int subchunk1Id = stream.readInt();
        if (subchunk1Id != ASCII_FMT) {
            throw new NotWavException();
        }
        header.setSubchunk1Id(subchunk1Id);
        header.setSubchunk1Size(Bytes.toBE(stream.readInt()));
        int audioFormat = Bytes.toBE((short) readShort(stream));
        if (audioFormat != FMT_PCM) {
            System.out.printf("%2x\n", audioFormat);
            throw new NotWavException();
        }
        header.setAudioFormat(audioFormat);
        header.setNumChannels(Bytes.toBE((short) readShort(stream)));
        header.setSampleRate(Bytes.toBE(stream.readInt()));
        header.setByteRate(Bytes.toBE(stream.readInt()));
        header.setBlockAlign(Bytes.toBE((short) readShort(stream)));
        header.setBitsPerSample(Bytes.toBE((short) readShort(stream)));

        int subchunk2Id = stream.readInt();
        /*if (subchunk2Id != ASCII_DATA) {
            throw new NotWavException();
        }*/
        header.setSubchunk2Id(subchunk2Id);
        header.setSubchunk2Size(Bytes.toBE(stream.readInt()));
        return header;
    }

}
