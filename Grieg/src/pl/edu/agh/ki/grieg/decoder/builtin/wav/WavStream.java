package pl.edu.agh.ki.grieg.decoder.builtin.wav;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import pl.edu.agh.ki.grieg.data.Format;
import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.DecodingAudioStream;
import pl.edu.agh.ki.grieg.utils.Bytes;
import pl.edu.agh.ki.grieg.utils.NotImplementedException;

class WavStream extends DecodingAudioStream {

    private static final float NORM = Short.MAX_VALUE;

    WavStream(DataInputStream stream, SourceDetails details) {
        super(stream, details);
        Format fmt = getFormat();
        if (fmt.bitDepth != 16) {
            throw new NotImplementedException(
                    "Sorry, cannot open " + fmt.bitDepth + "-bit file, only 16-bit sound is currently supported");
        }
    }


    @Override
    protected boolean readSingleSample(float[][] samples, int n)
            throws AudioException, IOException {
        try {
            for (int i = 0; i < samples.length; ++i) {
                short s = (short) Bytes.toBE(stream().readShort());
                samples[i++][n] = s / NORM;
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

}
