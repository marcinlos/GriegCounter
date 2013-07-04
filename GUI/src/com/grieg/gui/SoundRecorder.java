package com.grieg.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.grieg.gui.R;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

public class SoundRecorder extends Activity {

	private static final int RECORDER_BPP = 16;
	private static final String ID = "Recorder";
	private String mFileName = null;
	private String tmpFileName = null;
	private static final int RECORDER_SAMPLERATE = 44100;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

	private AudioRecord recorder = null;
	private int bufferSize = 0;
	private Thread recordingThread = null;
	private boolean isRecording = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(ID,"zaczal sie create recordera");
		setContentView(R.layout.activity_sound_recorder);
		Log.e(ID,"jest content view");
		// Show the Up button in the action bar.
		//setupActionBar();

		bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING);

		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		tmpFileName= Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/recorderAudio.wav";
		tmpFileName += "/recorderAudio.tmp";


	}


	public void startRecording(View v){
		boolean on = ((ToggleButton) v).isChecked();
		if (on) {
			File f = new File(mFileName);
			if(f.exists()){
				f.delete();
			}
			recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
					RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING, bufferSize);

			recorder.startRecording();

			isRecording = true;

			recordingThread = new Thread(new Runnable() {

				@Override
				public void run() {
					writeAudioDataToFile();
				}
			},"AudioRecorder Thread");

			recordingThread.start();

		} else {
			if(null != recorder){
				isRecording = false;

				recorder.stop();
				recorder.release();

				recorder = null;
				recordingThread = null;
			}

			copyWaveFile(tmpFileName,mFileName);
			deleteTempFile();

		}
	}

	private void deleteTempFile() {
		File file = new File(tmpFileName);
		file.delete();
	}

	private void copyWaveFile(String inFilename,String outFilename){
		FileInputStream in = null;
		FileOutputStream out = null;
		long totalAudioLen = 0;
		long totalDataLen = totalAudioLen + 36;
		long longSampleRate = RECORDER_SAMPLERATE;
		int channels = 2;
		long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;

		byte[] data = new byte[bufferSize];

		try {
			in = new FileInputStream(inFilename);
			out = new FileOutputStream(outFilename);
			totalAudioLen = in.getChannel().size();
			totalDataLen = totalAudioLen + 36;

			WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
					longSampleRate, channels, byteRate);

			while(in.read(data) != -1){
				out.write(data);
			}

			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			Log.e(ID,"exception",e);
		} catch (IOException e) {
			Log.e(ID,"exception",e);
		}
	}
	private void WriteWaveFileHeader(
			FileOutputStream out, long totalAudioLen,
			long totalDataLen, long longSampleRate, int channels,
			long byteRate) throws IOException {

		byte[] header = new byte[44];

		header[0] = 'R';  // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f';  // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1;  // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (2 * 16 / 8);  // block align
		header[33] = 0;
		header[34] = RECORDER_BPP;  // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

		out.write(header, 0, 44);
	}




	private void writeAudioDataToFile(){
		byte data[] = new byte[bufferSize];
		FileOutputStream os = null;

		try {
			os = new FileOutputStream(tmpFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(ID,"exception",e);
		}

		int read = 0;

		if(null != os){
			long time = System.currentTimeMillis();
			long temp;
			int count=0;
			long diff;
			while(isRecording){
				temp = time;
				time = System.currentTimeMillis();
				diff = time-temp;
				Log.e(ID, "time: " + diff);


				read = recorder.read(data, 0, bufferSize);

				if(AudioRecord.ERROR_INVALID_OPERATION != read){
					try {
						Log.e(ID,"rozmiar: " + read);
						os.write(data);
					} catch (IOException e) {
						Log.e(ID,"exception",e);
					}
				}
			}

			try {
				os.close();
			} catch (IOException e) {
				Log.e(ID,"exception",e);
			}
		}
	}


	public void analyze(View v){
		Intent intent = new Intent(this, SoundAnalyzer.class);
		intent.putExtra(SoundAnalyzer.EXTRA_MESSAGE, mFileName);
		Log.e(ID,"starting him");
		startActivity(intent);
		Log.e(ID,"Not sure if program flow goes here");
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
