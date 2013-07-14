package pl.edu.agh.ki.grieg.decoder.builtin.wav;

/**
 * Header of a WAV file contained in the RIFF. Provides information about the
 * audio data it contains.
 * 
 * @author los
 * 
 */
public class WavHeader {

    private short audioFormat;
    private int channels;
    private int sampleRate;
    private int byteRate;
    private short blockAlign;
    private short depth;

    public short getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(short audioFormat) {
        this.audioFormat = audioFormat;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getByteRate() {
        return byteRate;
    }

    public void setByteRate(int byteRate) {
        this.byteRate = byteRate;
    }

    public short getBlockAlign() {
        return blockAlign;
    }

    public void setBlockAlign(short blockAlign) {
        this.blockAlign = blockAlign;
    }

    public short getDepth() {
        return depth;
    }

    public void setDepth(short depth) {
        this.depth = depth;
    }

}
