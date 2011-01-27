package com.daneel.xmlrpc;


import android.os.Bundle;
import android.preference.PreferenceActivity;

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
    }

}