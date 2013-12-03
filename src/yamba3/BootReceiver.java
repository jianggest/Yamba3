package yamba3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		context.startService(new Intent(context, UpdaterService.class)); // 

		Log.d("BootReceiver", "onReceived");


	}

}
