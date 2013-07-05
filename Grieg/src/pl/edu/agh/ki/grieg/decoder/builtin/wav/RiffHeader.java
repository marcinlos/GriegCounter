package pl.edu.agh.ki.grieg.decoder.builtin.wav;

/**
 * Simple POJO class holding data present in the RIFF file header.
 * 
 * @author los
 */
class RiffHeader {

    private int chunkId;
    private int chunkSize;
    private int format;
    private int subchunk1Id;
    private int subchunk1Size;
    private int audioFormat;
    private int numChannels;
    private int sampleRate;
    private int byteRate;
    private int blockAlign;
    private int bitsPerSample;
    private int subchunk2Id;
    private int subchunk2Size;

    public int getChunkId() {
        return chunkId;
    }

    public void setChunkId(int chunkId) {
        this.chunkId = chunkId;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getSubchunk1Id() {
        return subchunk1Id;
    }

    public void setSubchunk1Id(int subchunk1Id) {
        this.subchunk1Id = subchunk1Id;
    }

    public int getSubchunk1Size() {
        return subchunk1Size;
    }

    public void setSubchunk1Size(int subchunk1Size) {
        this.subchunk1Size = subchunk1Size;
    }

    public int getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(int audioFormat) {
        this.audioFormat = audioFormat;
    }

    public int getNumChannels() {
        return numChannels;
    }

    public void setNumChannels(int numChannels) {
        this.numChannels = numChannels;
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

    public int getBlockAlign() {
        return blockAlign;
    }

    public void setBlockAlign(int blockAlign) {
        this.blockAlign = blockAlign;
    }

    public int getBitsPerSample() {
        return bitsPerSample;
    }

    public void setBitsPerSample(int bitsPerSample) {
        this.bitsPerSample = bitsPerSample;
    }

    public int getSubchunk2Id() {
        return subchunk2Id;
    }

    public void setSubchunk2Id(int subchunk2Id) {
        this.subchunk2Id = subchunk2Id;
    }

    public int getSubchunk2Size() {
        return subchunk2Size;
    }

    public void setSubchunk2Size(int subchunk2Size) {
        this.subchunk2Size = subchunk2Size;
    }

}
