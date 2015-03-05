package com.gw.smstransmit.broadcast;

import com.gw.smstransmit.service.SMSService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver{

	private final static String TAG = "BootReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Log.d(TAG, "boot");
		Intent smsIntent = new Intent(context, SMSService.class);
		context.startService(smsIntent);
	}

}
