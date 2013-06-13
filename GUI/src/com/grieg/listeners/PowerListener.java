package com.grieg.listeners;

public interface PowerListener {
	public static final int FIRST_CHANNEL = 1;
	public static final int SECOND_CHANNEL = 2;
	/**
	 * Should be called WaveCompressor.width-times, arguments are
	 * relative distances from the middle
	 * @param max - highest point, between -height/2 and height/2
	 * @param min - lowest point, between -height/2 and height/2
	 * @param channel - channel constant
	 */
	public void getPixelForPower(float min, float max, int channel);
}
