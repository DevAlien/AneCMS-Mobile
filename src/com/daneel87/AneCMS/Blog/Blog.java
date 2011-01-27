package com.daneel87.AneCMS.Blog;

import java.util.HashMap;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import com.daneel87.AneCMS.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Blog extends Activity {

	private String server;
	private String sessionid;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.blog);
	    
	    Bundle b = getIntent().getExtras();
	    server = b.getString("server");
	    sessionid = b.getString("sessionid");

	    loadPosts();
	    // TODO Auto-generated method stub
	}
	
	@SuppressWarnings("unchecked")
	private void loadPosts(){
		XMLRPCClient client = new XMLRPCClient(server + "/xmlrpc.php");
    	ListView list = (ListView) findViewById(R.id.ListViewPosts);
    	ArrayAdapter<String> adapter = null;
        try {
         	Object[] result = (Object[]) client.call("AneCMSBlog.getPosts", sessionid);
 			adapter = new ArrayAdapter<String>(this, R.layout.blog_list_item);
 			HashMap<String,String> resultmap;
 			for(int i=0;i<result.length;i++){
 				resultmap = (HashMap<String,String>) result[i];
 				adapter.add(resultmap.get("title"));
 			}

 		} catch (XMLRPCException e) {
 			e.printStackTrace();
 		}
 		list.setAdapter(adapter);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        try {
			inflater.inflate(R.menu.blog_menu, menu);
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
        case R.id.new_post:
        	Intent i = new Intent(this, BlogNewPost.class);
            startActivity(i);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
