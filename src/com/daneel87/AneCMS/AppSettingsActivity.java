package com.daneel87.AneCMS;


import java.util.HashMap;

import org.xmlrpc.android.XMLRPCClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

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
    	if (checkServer()){
    		finish();
    	}else{
    		Context context = getApplicationContext();
            CharSequence text = this.getString(R.string.server_not_valid);
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(context, text, duration).show();
    	}
		
	}
    
    public boolean checkServer(){
    	String v="";
        SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String Skey = getApplicationContext().getString(R.string.settings_server);
        try {
            v = sprefs.getString(Skey,"");
        } catch (ClassCastException e) {
            // if exception, do nothing; that is return default value of false.
        }
		XMLRPCClient client = new XMLRPCClient(v + "/xmlrpc.php");
		try {
			boolean result = (Boolean) client.call("AneCMS.check");
			return result;
		} catch (Exception e) {
			return false;
		}
	}
}