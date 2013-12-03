package yamba3;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

import yamba3.PrefsActivity;
import yamba3.StatusActivity;

import yamba3.UpdaterService;
import com.example.yamba3.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener,
TextWatcher{

	 private static final String TAG = "StatusActivity";
	 private static final String  CONSUMER_KEY = "2jLUkBEGuig9zWN94GBow";
	 private static final String  CONSUMER_SECRET ="qe0HAVVq4WYAKBkqrQEneVIosYJpTApdr47Jdf2GlnQ";
	  EditText editText;
	  
	  SharedPreferences prefs;

	  Button updateButton;

	  Twitter twitter;
	  
	  TextView textCount; // 

	 
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);
		
		// Find views

	    editText = (EditText) findViewById(R.id.editText); // 

	    updateButton = (Button) findViewById(R.id.buttonUpdate);

	 

	    updateButton.setOnClickListener(this); // 

	    textCount = (TextView) findViewById(R.id.textCount); // 

	    textCount.setText(Integer.toString(140)); // 

	    textCount.setTextColor(Color.GREEN); // 

	    editText.addTextChangedListener(this); // 


	    Log.d(TAG,"jacob onCreate");
	   
	    
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		// getTwitter();
		  Log.d(TAG,"jacob onClicked");
		  try {

			
			  String status = editText.getText().toString();
			  new PostToTwitter().execute(status); 

	     } catch (TwitterException e) {

	             Log.d(TAG, "Twitter setStatus failed: " + e);

	     }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		  MenuInflater inflater = getMenuInflater();   //  

		  inflater.inflate(R.menu.menu, menu);         // 

		  return true; // 

		}


	public boolean onOptionsItemSelected(MenuItem item) {
		
		  switch (item.getItemId()) {                              // 
		  	
		  case R.id.itemPrefs:
			  Toast.makeText(StatusActivity.this, "select the itemPrefs", Toast.LENGTH_LONG).show();
			  Log.d(TAG,"jacob onOptionsItemSelected");
		    startActivity(new Intent(this, PrefsActivity.class));  // 
		    	
		  break;
		  
		  case R.id.itemServiceStart:

			    startService(new Intent(this, UpdaterService.class)); //
			    break;

		  case R.id.itemServiceStop:

			    stopService(new Intent(this, UpdaterService.class));  //
			    break;

		  }

		 

		  return true;  // 

		}
	
	
	// TextWatcher methods

		  public void afterTextChanged(Editable statusText) { // 

		    int count = 140 - statusText.length(); // 

		    textCount.setText(Integer.toString(count));

		    textCount.setTextColor(Color.GREEN); // 

		    if (count < 10)

		      textCount.setTextColor(Color.YELLOW);

		    if (count < 0)

		      textCount.setTextColor(Color.RED);

		  }

		 

		  public void beforeTextChanged(CharSequence s, int start, int count, int after) { // {#10}

		  }

		 

		  public void onTextChanged(CharSequence s, int start, int before, int count) { // {#11}

		  }
		// Asynchronously posts to twitter

		  class PostToTwitter extends AsyncTask<String, Integer, String> { // 

		    // Called to initiate the background activity

		    @Override

		    protected String doInBackground(String... statuses) { // 

		      try {
		    	  twitter = ((YambaApplication)getApplication()).getTwitter();
		    	  
		    	  Twitter.Status  status = twitter.updateStatus(statuses[0]);

		          return status.text;

		      } catch (TwitterException e) {

		        Log.e(TAG, e.toString());

		        e.printStackTrace();

		        return "Failed to post";

		      }

		    }

		 

		    // Called when there's a status to be updated

		    @Override

		    protected void onProgressUpdate(Integer... values) { // 

		      super.onProgressUpdate(values);

		      // Not used in this case

		    }

		 

		    // Called once the background activity has completed

		    @Override

		    protected void onPostExecute(String result) { // 

		      Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();

		    }

		  }
}
