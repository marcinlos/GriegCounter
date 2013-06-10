package com.grieg.gui;



import com.grieg.gui.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {
	
	private static final int PICKFILE_RESULT_CODE = 1;
	private static final String ID = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    
    public void openRecorder(View view){
    	Intent intent = new Intent(this, SoundRecorder.class);
    	startActivity(intent);
    }
    
    public void openFile(View view){
    	Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
        fileintent.setType("file/*");
        try {
            startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
        } catch (ActivityNotFoundException e) {
            Log.e(ID,"Dupa");
        }
    	//if startActivity works well it will call onActivityResults
    	//which will do the rest of this function
    	//if it does not - suxx
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	switch(requestCode){
    	case PICKFILE_RESULT_CODE:
    		if(resultCode==RESULT_OK){
    			String filePath = data.getData().getPath();
    			Intent i = new Intent(this,SoundAnalyzer.class);
    			i.putExtra(SoundAnalyzer.EXTRA_MESSAGE, filePath);
    			startActivity(i);
    		}
    		else{
    			Log.e(ID,"Nie wczytal sie ziom");
    		}
    		break;

    	}
    }

    public void endProgram(View v){
    	finish();
    	System.exit(0);
    }

    
}
