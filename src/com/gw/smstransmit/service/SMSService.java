package com.gw.smstransmit.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gw.smstransmit.db.PhoneSqlite;
import com.gw.smstransmit.main.MainActivity;
import com.gw.smstransmit.model.SMSMsg;
import com.gw.transmit.adapter.PhoneAdapter;
import com.quanta.async.QuantaAppUtil;

import android.R.string;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;

public class SMSService extends Service{

	public final static String ACTION_RECEIVER = "com.gw.smstransmit.action.smsreceiver";
	private final static String TAG = "SMSService";
	private SmsObserver smsObserver;
	private Uri SMS_INBOX = Uri.parse("content://sms/");
	private PhoneSqlite sqlite;
	private ArrayList<SMSMsg> dataList;
	private SharedPreferences preference;
	private String destNumber;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate(){
		smsObserver = new SmsObserver(this, smsHandler);  
		sqlite = new PhoneSqlite(this);
		preference = QuantaAppUtil.getSharedPreferences(this);
		destNumber = preference.getString("_dest_number", "15602221486");
		assignData();
		
        getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);
        IntentFilter filter = new IntentFilter(ACTION_RECEIVER);
        registerReceiver(smsReceiver, filter);
	}
	
	public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "============> SMSService.onStart");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(smsReceiver);
	}
	
	/**
	 * 数据库改变之后需要重新赋值
	 */
	private void assignData(){
		ArrayList<HashMap<String, String>> mapList = sqlite.select(null, null);
		try {
			dataList = (ArrayList<SMSMsg>) QuantaAppUtil.hashMapToModel("SMSMsg", mapList);
		} catch (Exception e) {
			dataList = new ArrayList<SMSMsg>();
			e.printStackTrace();
		}
	}

	/**
	 * 查询是否有新信息来
	 * @param address
	 */
	public void getSmsFromPhone(String address) {
		ContentResolver cResolver = getContentResolver();
		String[] projection = new String[] { "_id", "address", "person", "date", "type", "body" };//
		String where = " address=? and read=?";
		String[] args = new String[]{address, "0"}; 
		Cursor cursor = cResolver.query(SMS_INBOX, projection, where, args, "date desc");
		if (null == cursor){
			return;
		}
		while (cursor.moveToNext()) {
			String number = cursor.getString(cursor.getColumnIndex("address"));//手机号
			String name = cursor.getString(cursor.getColumnIndex("person"));//联系人姓名列表
			String body = cursor.getString(cursor.getColumnIndex("body"));
			
			ContentValues values = new ContentValues();
			values.put("read", 1);
			cResolver.update(SMS_INBOX, values, "_id=?", new String[]{cursor.getString(0)});
			
//			send("15602221489", "我收到了，臭屁股");
			send(destNumber, body);
			Log.d(TAG, body);
			
			//获取自己短信服务号码中的验证码
			Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");
			Matcher matcher = pattern.matcher(body);
			if (matcher.find()) {
				String res = matcher.group().substring(1, 11);
				Log.d(TAG, res);
			}
		}
		cursor.close();
	}
	
	/**
	 * 发送短信
	 * @param phone
	 * @param message
	 */
	private void send(String phone, String message){
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, message, null, null);
    }
	
	public Handler smsHandler = new Handler() {
		//这里可以进行回调的操作
		
		public void handleMessage(Message msg) {
			Log.d(TAG, "handler1");
		}
	};
	
	class SmsObserver extends ContentObserver {

		public SmsObserver(Context context, Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			for (SMSMsg smsMsg : dataList) {
				getSmsFromPhone(smsMsg.getPhone());
			}
		}
	}
	
	BroadcastReceiver smsReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "smsreceiver");
			Bundle bundle = intent.getExtras();
			switch (bundle.getInt("type")) {
			case 1:
				//更新需要转发的号码列表
				assignData();
				break;
			case 2:
				//跟新转发人的号码
				destNumber = preference.getString("_dest_number", "15602221486");
				break;

			default:
				break;
			}
		}
	};
}
