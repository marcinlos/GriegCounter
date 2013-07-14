package pl.edu.agh.ki.grieg.decoder.builtin.wav;

import java.io.EOFException;
import java.io.IOException;

import pl.edu.agh.ki.grieg.data.Format;
import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.decoder.util.PCM;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.DecodingAudioStream;
import pl.edu.agh.ki.grieg.utils.BinaryInputStream;
import pl.edu.agh.ki.grieg.utils.NotImplementedException;

class WavStream extends DecodingAudioStream {

    WavStream(BinaryInputStream stream, SourceDetails details) {
        super(stream, details);
        Format fmt = getFormat();
        if (fmt.bitDepth != 16) {
            throw new NotImplementedException("Sorry, cannot open "
                    + fmt.bitDepth
                    + "-bit file, only 16-bit sound is currently supported");
        }
    }

    @Override
    protected boolean readSingleSample(float[][] samples, int n)
            throws AudioException, IOException {
        try {
            for (int i = 0; i < samples.length; ++i) {
                short s = stream().readShortLE();
                samples[i][n] = PCM.fromShort(s);
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

}
