package yamba3;


import com.example.yamba3.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TimelineActivity extends BaseActivity {
	
	DbHelper dbHelper;
	SQLiteDatabase db;
	Cursor	cursor;
	//TextView textTimeline;
	ListView listTimeline;
	SimpleCursorAdapter adapter;
	IntentFilter	filter;
	TimelineReceiver receiver;
	static final String[] FROM={DbHelper.C_CREATED_AT,DbHelper.C_USER,DbHelper.C_TEXT};
	static final int[] TO = {R.id.textCreatedAt,R.id.textUser,R.id.textText};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.timeline_basic);
		
		//find your views
		//textTimeline = (TextView)findViewById(R.id.textTimeline);
		listTimeline = (ListView)findViewById(R.id.listTimeline);
		
		//Connect to database
		dbHelper = new DbHelper(this);
		db = dbHelper.getReadableDatabase();
		
		//startService(new Intent(this, UpdaterService.class));
		
		receiver = new TimelineReceiver();
		filter = new IntentFilter("com.marakana.yamba.NEW_STATUS"); 
	}
	
	
	@Override

	protected void onPause() {

	  super.onPause();

	 

	  // UNregister the receiver

	  unregisterReceiver(receiver);  // 

	}


	
	@Override
	public void onDestroy(){
		
		super.onDestroy();
		
		//close the database
		
		db.close();
	}
	
	@Override
	protected void onResume(){
		
		super.onResume();
		
		//Get the data form the database
		
		cursor = db.query(DbHelper.TABLE, null, null, null, null, null,DbHelper.C_CREATED_AT + " DESC");
		startManagingCursor(cursor);
		
		//Setup the adapter
		adapter = new SimpleCursorAdapter(this,R.layout.row,cursor,FROM,TO);
		listTimeline.setAdapter(adapter); 
		
		// Register the receiver

		  registerReceiver(receiver, filter);   

		  
		//Iterate over all the data and print it out
		//String user,text,output;
		//while(cursor.moveToNext()){
			
		//	user = cursor.getString(cursor.getColumnIndex(DbHelper.C_USER));
			
		//	text = cursor.getString(cursor.getColumnIndex(DbHelper.C_TEXT));

			  
		//	output = String.format("%s: %s\n", user,text);
			
			//textTimeline.append(output);
		//}
		
	}
	
	class TimelineReceiver extends BroadcastReceiver { // 

		  @Override

		  public void onReceive(Context context, Intent intent) { // 

		    cursor.requery(); // 

		    adapter.notifyDataSetChanged(); // 

		    Log.d("TimelineReceiver", "onReceived");

		  }

		}
}


