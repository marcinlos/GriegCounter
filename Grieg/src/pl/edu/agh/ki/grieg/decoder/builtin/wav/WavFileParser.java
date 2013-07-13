package pl.edu.agh.ki.grieg.decoder.builtin.wav;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import pl.edu.agh.ki.grieg.data.Format;
import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.riff.ChunkHeader;
import pl.edu.agh.ki.grieg.decoder.riff.NotRiffException;
import pl.edu.agh.ki.grieg.decoder.riff.RiffHeader;
import pl.edu.agh.ki.grieg.decoder.riff.RiffParser;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFileParser;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.meta.SimpleTagContainer;
import pl.edu.agh.ki.grieg.meta.TagSet;
import pl.edu.agh.ki.grieg.utils.BinaryInputStream;


public class WavFileParser implements AudioFileParser {

    private static final Iterable<String> EXTS = Arrays.asList("wav");

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
        RiffParser riff = readRiffHeader(stream);
        readFormatHeader(riff);
        WavHeader wav = readWavHeader(stream);
        ChunkHeader dataHeader = findDataHeader(riff);
        int channels = wav.getChannels();
        int bitsPerSample = wav.getDepth();
        long size = dataHeader.getSize();
        long sampleCount = size / (channels * bitsPerSample / 8);
        int sampleRate = wav.getSampleRate();
        float length = sampleCount / (float) sampleRate;
        Format format = new Format(channels, bitsPerSample, sampleRate);
        TagSet tags = new SimpleTagContainer();
        return new SourceDetails(null, length, sampleCount, format, tags);
    }

    private ChunkHeader findDataHeader(RiffParser riff) throws IOException,
            NotWavException {
        try {
            while (true) {
                ChunkHeader dataHeader = riff.readChunkHeader();
                if (dataHeader.getId() == ASCII_DATA) {
                    return dataHeader;
                } else {
                    System.out.println("Omitting " + dataHeader.idAsString());
                    riff.getStream().skip(dataHeader.getSize());
                }
            }
        } catch (EOFException e) {
            // no 'data' chunk - not a wav file
            throw new NotWavException("Missing 'data' chunk");
        }
    }

    private void readFormatHeader(RiffParser riff) throws IOException,
            NotWavException {
        ChunkHeader formatHeader = riff.readChunkHeader();
        if (formatHeader.getId() != ASCII_FMT) {
            throw new NotWavException("Invalid chunk ID: '"
                    + formatHeader.idAsString() + "'");
        }
    }

    private RiffParser readRiffHeader(InputStream stream)
            throws NotRiffException, IOException, NotWavException {
        RiffParser riff = new RiffParser(stream);
        RiffHeader header = riff.readRiffHeader();
        if (header.getFormat() != ASCII_WAVE) {
            throw new NotWavException("Invalid format: " + header.getFormat());
        }
        return riff;
    }

    private WavHeader readWavHeader(InputStream stream) throws IOException,
            NotWavException {
        WavHeader header = new WavHeader();
        BinaryInputStream input = new BinaryInputStream(stream);
        short audioFormat = input.readShortLE();
        if (audioFormat != FMT_PCM) {
            throw new NotWavException("Unsupported audio format: "
                    + audioFormat);
        }
        header.setAudioFormat(audioFormat);
        header.setChannels(input.readShortLE());
        header.setSampleRate(input.readIntLE());
        header.setByteRate(input.readIntLE());
        header.setBlockAlign(input.readShortLE());
        header.setDepth(input.readShortLE());
        return header;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AudioFile open(InputStream stream) throws DecodeException,
            IOException {
        SourceDetails details = getDetails(stream);
        AudioStream audioStream = new WavStream(new BinaryInputStream(stream),
                details);
        return new AudioFile(details, audioStream);
    }

}
