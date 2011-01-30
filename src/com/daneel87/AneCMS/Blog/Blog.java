package com.daneel87.AneCMS.Blog;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import com.daneel87.AneCMS.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

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
	    

	    final ListView list = (ListView) findViewById(R.id.BlogListViewPosts);
        list.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        		@SuppressWarnings("unchecked")
				HashMap<String,String> item = (HashMap<String,String>) list.getItemAtPosition(position);
        		Intent i = new Intent(Blog.this, BlogPost.class);
        		Bundle b = new Bundle();
        		b.putString("titolo", item.get("title"));
        		b.putString("contenuto", item.get("article") + item.get("art_more"));
        		i.putExtras(b); //Put your id to your next Intent
               startActivity(i);
        	}
        	});
	}
	
	@SuppressWarnings("unchecked")
	private void loadPosts(){
		XMLRPCClient client = new XMLRPCClient(server + "/xmlrpc.php");
    	ListView list = (ListView) findViewById(R.id.BlogListViewPosts);
    	PostAdapter adapter = null;
        try {
         	Object[] result = (Object[]) client.call("AneCMSBlog.getPosts", sessionid);
         	ArrayList<HashMap<String,String>> resultarray = new ArrayList<HashMap<String,String>>();
 			HashMap<String,String> resultmap;
 			for(int i=0;i<result.length;i++){
 				resultmap = (HashMap<String,String>) result[i];
 				resultarray.add(resultmap);
 			}
 			adapter = new PostAdapter(this, R.layout.blog_list_item, resultarray);
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
        	Bundle b = new Bundle();
    		b.putString("sessionid", sessionid);
    		b.putString("server", server);
    		i.putExtras(b);
            startActivity(i);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private class PostAdapter extends ArrayAdapter<HashMap<String,String>> {

        private ArrayList<HashMap<String,String>> items;

        public PostAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String,String>> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.blog_list_item, null);
                }
                Map<String,String> o = items.get(position);
                if (o != null) {
                        TextView pt = (TextView) v.findViewById(R.id.PostTitle);
                        TextView pa = (TextView) v.findViewById(R.id.PostAutor);
                        TextView pd = (TextView) v.findViewById(R.id.PostDate);
                        if (pt != null) {
                            pt.setText(o.get("title"));                            
                        }
                        if(pa != null){
                            pa.setText(o.get("username"));
                        }
                        if(pd != null){
                        	pd.setText(new Timestamp((Long.decode(o.get("insert_date")))*1000).toLocaleString());
                        }
                }
                return v;
        }
}
}
