package pl.edu.agh.ki.grieg.android.misc;

public enum Dictionary {
	PREFS("grieg"),
	ONLY_OFFLINE("only_offline"),
	FILE_CHOSEN("file_chosen");
	
	
	private String caption;
	private Dictionary(String s){
		caption = s;
	}
	
	public String getCaption(){
		return caption;
	}

}
