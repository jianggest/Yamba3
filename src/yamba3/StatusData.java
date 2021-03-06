package yamba3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StatusData {

	private static final String TAG = StatusData.class.getSimpleName();
	
	static final int VERSION = 1;
	static final String DATABASE = "timeline.db";
	static final String TABLE = "timeline";
	
	public static final String C_ID ="_id";
	public static final String C_CREATED_AT = "created_at";
	public static final String C_TEXT="txt";
	public static final String C_USER ="user";
	
	
	private static final String GET_ALL_ORDER_BY =
			C_CREATED_AT + " DESC";
	
	private static final String[] MAX_CREATED_AT_COLUMNS =
		{"max(" + StatusData.C_CREATED_AT + ")" };
	
	private static final String[] DB_TEXT_COLUMNS =
		{C_TEXT};
	
	//DbHleper implementations
	class DbHelper extends SQLiteOpenHelper{
		
		public DbHelper(Context context){
			super(context,DATABASE,null,VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db){
			Log.i(TAG,"Creatting database:"+ DATABASE);
			//db.execSQL("create table "+ TABLE +"("+C_ID+" int primary key,"
			//		+C_CREATED_AT + " int, "+ C_USER+" text, "+C_TEXT+" text)");
			
			
			db.execSQL("create table " + TABLE + " (" + C_ID + " int primary key, "
					+ C_CREATED_AT + " int, " + C_USER + " text, " + C_TEXT + " text)");


		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion)
		{
			db.execSQL("drop table "+ TABLE);
			this.onCreate(db);
		}
		
	}
	
	private final DbHelper dbHelper;
	
	
	public StatusData(Context context){
		this.dbHelper = new DbHelper(context);
		Log.i(TAG,"Initialized data");
	}
	
	public void close(){
		this.dbHelper.close();
	}
	
	
	
	
	public void inserOrIgnore(ContentValues values){
		Log.d(TAG,"insertOrIgnore on "+ values);
		
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		
		try {
			db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		}finally{
			db.close();
		}
	}
	
	//return Cursor where the clolumns are _id,create_at,user,txt
	
	public Cursor getSatausUpdates(){
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		
		return db.query(TABLE,null,null,null,null,null,GET_ALL_ORDER_BY);
		
	}
	
	//return Timestamp of the latest staus we have it in the database
	
	public long getLatestStatusCreateAtTime(){
	
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		Log.d(TAG,"jacob get cursor getLatestStatusCreateAtTime");
		
		try{
			Cursor cursor = db.query(TABLE, MAX_CREATED_AT_COLUMNS, null, null, null, null, null);
			try{
				return cursor.moveToNext()?cursor.getLong(0):Long.MIN_VALUE;
			}finally{
				Log.d(TAG,"jacob get cursor finally");
				cursor.close();
			}
		}finally{
			Log.d(TAG,"Jacob getLastStatusFinally");
			db.close();
		}
	}
	
	//@parm id of the status we are looking for
	//@rturn Text of the status
	public String getStatusTextById(long id){
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		
		try{
			Cursor cursor = db.query(TABLE, DB_TEXT_COLUMNS, C_ID+"="+id, null, null, null, null);
			
			try{
				return cursor.moveToNext()?cursor.getString(0):null;
				
			}finally{
				cursor.close();
			}
		}finally{
			db.close();
		}
	}
	
	public void delete() {
	    // Open Database
	    SQLiteDatabase db = dbHelper.getWritableDatabase();

	    // Delete the data
	    db.delete(TABLE, null, null);

	    // Close Database
	    db.close();
	  }
}
