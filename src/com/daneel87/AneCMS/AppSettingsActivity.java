package com.daneel87.AneCMS;


import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;

public class AppSettingsActivity extends PreferenceActivity {

    /**
     * Default Constructor
     */
	public AppSettingsActivity() {
	}
   /** 
    * Called when the activity is first created. 
    * 	Inflate the Preferences Screen XML declaration.
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings); // Inflate the XML declaration
        setContentView(R.layout.settings);
    }

    public void Save(View v) {
		finish();
	}
}