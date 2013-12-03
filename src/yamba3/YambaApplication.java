package yamba3;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class YambaApplication extends Application implements
	OnSharedPreferenceChangeListener{

	private boolean serviceRunning;
	private static final String TAG = "testactivty";//YambaApplication.class.getSimpleName();
	public Twitter twitter;
	private SharedPreferences prefs;
	private StatusData statusData;
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		
		statusData = new StatusData(this);
		Log.i(TAG,"onCreated");
		
		
	}
	
	public StatusData getStatusData(){
		return statusData;
	}
	
	public boolean isServiceRunning() { // 

	    return serviceRunning;

	    }

	 

	  public void setServiceRunning(boolean serviceRunning) { // 

	    this.serviceRunning = serviceRunning;

	    }


	  
	@Override
	public void onTerminate(){
		super.onTerminate();
		 Log.i(TAG,"onTerminated");
		 
	}
	public synchronized Twitter getTwitter(){
		if(this.twitter == null){
			String username = this.prefs.getString("username", "student");
			String password = this.prefs.getString("password", "password");
			String apiRoot = prefs.getString("apiRoot", "http://yamba.marakana.com/api");
			
			Log.i(TAG,"uname:"+username+"paswd:"+password);
			Log.d(TAG,"jacob set right?"+apiRoot.toString());
			
			
			if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
					&& !TextUtils.isEmpty(apiRoot)){
				this.twitter = new Twitter(username,password);
				this.twitter.setAPIRootUrl(apiRoot);
			}
		}
		
		return this.twitter;
	}
	


	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub
		this.twitter = null;
	} 
	
	//Connects to the online service and puts the latest statuses into DB.
	//Returns the count of new stauses
	
	public synchronized int fetchStatusUpdates(){
		Log.d(TAG,"Fetching status updates");
		
		Twitter twitter = this.getTwitter();
		if(twitter == null){
			Log.d(TAG,"Twitter connection info not initialized");
			return 0;
		}
		try{
			int count = 0;
			
			List<Status> statusUpdates = twitter.getFriendsTimeline();
			
			long latestStatusCreatedAtTime = this.getStatusData().getLatestStatusCreateAtTime();
	
			ContentValues values = new ContentValues();
			
			
			
			for(Status status:statusUpdates){
				values.put(StatusData.C_ID,status.getId());
				
				long createdAt = status.getCreatedAt().getTime();
				values.put(StatusData.C_CREATED_AT, createdAt);
				values.put(StatusData.C_TEXT, status.getText());
				values.put(StatusData.C_USER, status.getUser().getName());
				
				Log.d(TAG,"Got update with id "+status.getId()+". Saving");
				this.getStatusData().inserOrIgnore(values);
				
				if(latestStatusCreatedAtTime < createdAt)
				{
					count++;
				}
			}
			
			Log.d(TAG,count>0?"Got"+count + " Status updates":"No new status updatas");
			
			Log.d(TAG,"fetchStatusUpdates successful");
			return count;
		}catch (RuntimeException e){
			Log.e(TAG,"Failed to fetch status updates",e);
			return 0;
		}
	}
}

	