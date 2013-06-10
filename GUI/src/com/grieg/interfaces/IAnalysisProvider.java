package com.grieg.interfaces;

import com.grieg.listeners.WaveListener;

public interface IAnalysisProvider {
	
	/**
	 * adds Wave Compressor implementation to provider. To work in UI requires WaveListener.
	 * @param wc
	 */
	public void addCompressor(WaveCompressor wc);
	/**
	 * adds listener for waveform drawing.
	 * @param wl
	 */
	public void addWaveListener(WaveListener wl);
	
	/**
	 * 
	 * @return Number of channels in loaded audio file (1 or 2)
	 */
	public int getChannelNumber();

}
