package com.daneel87.AneCMS.Blog;

import com.daneel87.AneCMS.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.webkit.WebView;
import android.widget.TextView;

public class BlogPost extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.blog_post_view);
	    
	    TextView titolo = (TextView)findViewById(R.id.ViewPostTitle);
	    WebView contenuto = (WebView)findViewById(R.id.PostContent);
	    
	    Bundle b = getIntent().getExtras();
	    titolo.setText(b.getString("titolo"));
	    contenuto.loadData(b.getString("contenuto"), "text/html", "utf-8");
	    
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        try {
			inflater.inflate(R.menu.blog_post_menu, menu);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return true;
    }
}
