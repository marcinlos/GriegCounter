package pl.edu.agh.ki.grieg.android.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.android.misc.Dictionary;
import pl.edu.agh.ki.grieg.android.misc.FolderPicker;
import roboguice.activity.RoboActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;

public class GriegMain extends RoboActivity implements OnClickListener {

	private static final Logger logger = LoggerFactory
			.getLogger(GriegMain.class);
	
	private CheckBox checkBox;
	SharedPreferences prefs;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grieg_main);
		checkBox = (CheckBox) findViewById(R.id.checkbox);

		logger.debug("GriegMain activity created");
		
		
		prefs = this.getSharedPreferences(Dictionary.PREFS.getCaption(),Context.MODE_PRIVATE);
		
	}

	public void openFile(View v) {
		FolderPicker p = new FolderPicker(this, this, 0, true);
		p.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		FolderPicker picker = (FolderPicker) dialog;
		if (which == DialogInterface.BUTTON_POSITIVE) {
			picker.dismiss();
			String file_chosen = picker.getPath();
			logger.error(file_chosen);
			if(checkBox.isChecked()){
				prefs.edit().putBoolean(Dictionary.ONLY_OFFLINE.getCaption(), true).commit();
			}
			else{
				prefs.edit().putBoolean(Dictionary.ONLY_OFFLINE.getCaption(), false).commit();
			}
			
			Intent i = new Intent(this, DefaultBActivity.class);
			i.putExtra(Dictionary.FILE_CHOSEN.getCaption(),file_chosen);
	        startActivity(i);
	        //finish();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present

		getMenuInflater().inflate(R.menu.grieg_main, menu);
		return true;
	}

}
