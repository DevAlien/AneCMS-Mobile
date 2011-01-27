package com.daneel87.AneCMS;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import com.daneel87.AneCMS.Blog.Blog;

public class Main extends Activity {
	
	private String username;
	private String password;
	private String server;
	private String sessionid;
	static private Context appContext;
	
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = this;
        setContentView(R.layout.main);
        
        loadSettings();
        
        final ListView list = (ListView) findViewById(R.id.ListView01);
        list.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        	String item = (String) list.getItemAtPosition(position);
        	if (item == Main.this.getString(R.string.blog)){
        		Intent i = new Intent(Main.this, Blog.class);
        		Bundle b = new Bundle();
        		b.putString("sessionid", sessionid);
        		b.putString("server", server);
        		i.putExtras(b); //Put your id to your next Intent
                startActivity(i);
        	}
        	}
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        try {
			inflater.inflate(R.menu.main_menu, menu);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.settings:
        	Intent i = new Intent(this, AppSettingsActivity.class);
            startActivity(i);
            return true;
        case R.id.update:
        	loadSettings();
        	getServices();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public String getSetting(int key) {
    	String v="";
        SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        String Skey = appContext.getString(key);
        try {
            v = sprefs.getString(Skey,"");
        } catch (ClassCastException e) {
            // if exception, do nothing; that is return default value of false.
        }
        return v;
    }
    
    @SuppressWarnings("unchecked")
	private void getServices(){
    	XMLRPCClient client = new XMLRPCClient(server + "/xmlrpc.php");
    	ListView list = (ListView) findViewById(R.id.ListView01);
    	ArrayAdapter<String> adapter = null;
        try {
         	Object[] result = (Object[]) client.call("AneCMS.getModulesWS", sessionid);
 			adapter = new ArrayAdapter<String>(this, R.layout.list_item);
 			HashMap<String,String> resultmap;
 			String modulo = "";
 			for(int i=0;i<result.length;i++){
 				resultmap = (HashMap<String,String>) result[i];
 				modulo = resultmap.get("name");
 				if(modulo.equalsIgnoreCase("blog")){
 					adapter.add(this.getString(R.string.blog));
 				}
 				/*if(modulo.equalsIgnoreCase("register")){
 					adapter.add(this.getString(R.string.register));
 				}
 				if(modulo.equalsIgnoreCase("users")){
 					adapter.add(this.getString(R.string.users));
 				}
 				if(modulo.equalsIgnoreCase("home")){
 					adapter.add(this.getString(R.string.home));
 				}*/
 				
 			}

 		} catch (XMLRPCException e) {
 			e.printStackTrace();
 		}
 		list.setAdapter(adapter);
 		Context context = getApplicationContext();
        CharSequence text = this.getString(R.string.services_updated);
        int duration = Toast.LENGTH_LONG;
        Toast.makeText(context, text, duration).show();
    }
    
    public void loadSettings(){
    	boolean changed = false;
    	
    	changed = !getSetting(R.string.settings_username).equals(username);
        username = getSetting(R.string.settings_username);
        
        if(!changed)
        	changed = !getSetting(R.string.settings_password).equals(password);
        password = getSetting(R.string.settings_password);
        
        if(!changed)
        	changed = !getSetting(R.string.settings_server).equals(server);
        server = getSetting(R.string.settings_server);
        
        if(server == "" || username == "" || password == ""){
        	Intent i = new Intent(this, AppSettingsActivity.class);
            startActivity(i);
        	return;
        }
        
        if (changed || sessionid == null){
        	login();
        	getServices();
        }
    }
    
	private boolean login(){
    	
    	XMLRPCClient client = new XMLRPCClient(server + "/xmlrpc.php");
        try {
         	String result = (String) client.call("AneCMS.login",new String[] {username, password});
 			sessionid = result.toString();
 			return true;	
 		} catch (Exception e) {
 			sessionid = null;
 			Context context = getApplicationContext();
	        CharSequence text = this.getString(R.string.login_error);
	        int duration = Toast.LENGTH_LONG;
	        Toast.makeText(context, text, duration).show();
			return false;
 		}
    }
    
}