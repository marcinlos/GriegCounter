package pl.edu.agh.ki.grieg.decoder.builtin.wav;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.data.Format;
import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.riff.ChunkHeader;
import pl.edu.agh.ki.grieg.decoder.riff.NotRiffException;
import pl.edu.agh.ki.grieg.decoder.riff.RiffHeader;
import pl.edu.agh.ki.grieg.decoder.riff.RiffParser;
import pl.edu.agh.ki.grieg.decoder.util.PCM;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.meta.SimpleTagContainer;
import pl.edu.agh.ki.grieg.meta.TagSet;
import pl.edu.agh.ki.grieg.utils.BinaryInputStream;
import pl.edu.agh.ki.grieg.utils.NotImplementedException;

class WavStream implements AudioStream {

    // WavStream(BinaryInputStream stream, SourceDetails details) {
    /*
     * Format fmt = getFormat(); if (fmt.bitDepth != 16) { throw new
     * NotImplementedException("Sorry, cannot open " + fmt.bitDepth +
     * "-bit file, only 16-bit sound is currently supported"); }
     */
    // }

    private static final int ASCII_WAVE = 0x57415645;
    private static final int ASCII_FMT = 0x666d7420;
    private static final int ASCII_DATA = 0x64617461;

    private static final int FMT_PCM = 1;

    private interface PCMReader {

        float readSample(BinaryInputStream stream) throws IOException;
    }

    private static final PCMReader PCM8 = new PCMReader() {
        @Override
        public float readSample(BinaryInputStream stream) throws IOException {
            return PCM.fromByte((byte) stream.read());
        }
    };

    private static final PCMReader PCM16 = new PCMReader() {
        @Override
        public float readSample(BinaryInputStream stream) throws IOException {
            return PCM.fromShort(stream.readShortLE());
        }
    };

    private BinaryInputStream stream;

    private PCMReader converter;

    private SourceDetails details;
    private long remains;

    public WavStream(InputStream stream) throws DecodeException, IOException {
        this.stream = new BinaryInputStream(stream);
        details = getDetails(stream);
        remains = details.getSampleCount();
    }

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
        Format format = new Format(channels, sampleRate);
        TagSet tags = new SimpleTagContainer();
        converter = chooseConverter(bitsPerSample);
        return new SourceDetails(null, length, sampleCount, format, tags);
    }

    private PCMReader chooseConverter(int depth) {
        switch (depth) {
        case 8:
            return PCM8;
        case 16:
            return PCM16;
        default:
            throw new NotImplementedException("Sorry, cannot open " + depth
                    + "-bit file, only 16-bit sound is currently supported");
        }
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

    @Override
    public void close() throws IOException {
        stream.close();
    }

    @Override
    public int readSamples(float[][] buffer) throws AudioException, IOException {
        int count = buffer[0].length;
        int i;
        for (i = 0; --remains >= 0 && i < count
                && readSingleSample(buffer, i++);)
            ;
        return i;
    }

    protected boolean readSingleSample(float[][] samples, int n)
            throws AudioException, IOException {
        try {
            for (int i = 0; i < samples.length; ++i) {
                samples[i][n] = converter.readSample(stream);
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

    SourceDetails getDetails() {
        return details;
    }

    @Override
    public Format getFormat() {
        return details.getFormat();
    }

}
