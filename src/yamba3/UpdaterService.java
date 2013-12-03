package yamba3;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {

	static final String TAG = "UpdaterService"; //
	static final int DELAY = 60000; // a minute 
	
	private YambaApplication yamba;
	private boolean runFlag = false;  // 

	private Updater updater;

	DbHelper dbHelper; //

	SQLiteDatabase db;
	public static final String NEW_STATUS_INTENT = "com.marakana.yamba.NEW_STATUS";
	public static final String NEW_STATUS_EXTRA_COUNT = "NEW_STATUS_EXTRA_COUNT";


	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "onCreated");
		
		this.yamba = (YambaApplication) getApplication(); 
		this.updater = new Updater(); // 

		dbHelper = new DbHelper(this); //

		 
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		
		this.runFlag = false; // 

	    this.updater.interrupt(); // 

	    this.updater = null;
	    
	    this.yamba.setServiceRunning(false); 

	    
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		Log.d(TAG, "onStarted");

		super.onStartCommand(intent, flags, startId);
		
		this.runFlag = true; // 
		this.updater.start();
		this.yamba.setServiceRunning(true); 
		
		return  START_STICKY;


	}

	 /**

	   * Thread that performs the actual update from the online service

	   */

	  private class Updater extends Thread {  // 

		  List<Twitter.Status> timeline; // 
		  Intent intent;

		  


	    public Updater() {

	      super("UpdaterService-Updater");  // 

	    }

	 

	    @Override

	    public void run() { // 

	      UpdaterService updaterService = UpdaterService.this;  // 

	      while (updaterService.runFlag) {  // 

	        Log.d(TAG, "Updater running");

	        try {

	          // Some work goes here...
	        	// Get the timeline from the cloud
	        	YambaApplication yamba = (YambaApplication)updaterService.getApplication();
	        	int newUpdates = yamba.fetchStatusUpdates();
	        	if(newUpdates > 0)
	        	{
	        		Log.d(TAG,"We have a new status");
	        		
	        		 intent = new Intent(NEW_STATUS_INTENT); // 

	                 intent.putExtra(NEW_STATUS_EXTRA_COUNT, newUpdates); // 

	                 updaterService.sendBroadcast(intent); // 


	        	}
	        	Thread.sleep(DELAY);
	            
	        } catch (InterruptedException e) {  // 

	          updaterService.runFlag = false;

	        }

	      }

	    }

	  } // Updater


}
