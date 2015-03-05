package com.gw.smstransmit.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gw.smstransimit.R;
import com.gw.smstransmit.db.PhoneSqlite;
import com.gw.smstransmit.model.SMSMsg;
import com.gw.smstransmit.service.SMSService;
import com.gw.transmit.adapter.PhoneAdapter;
import com.quanta.async.QuantaAppUtil;

import android.app.Activity;
import android.app.PendingIntent;
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
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private EditText originEdit;
	private Button originBtn;
	
	private EditText destEdit;
	private Button destBtn;
	
	private ListView listView;
	private ArrayList<SMSMsg> dataList;
	private PhoneAdapter phoneAdapter;
	
	private PhoneSqlite sqlite;
	SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "oncreate");
		
		originEdit = (EditText)findViewById(R.id.origin_edit);
		originBtn = (Button)findViewById(R.id.origin_add);
		destEdit = (EditText)findViewById(R.id.dest_edit);
		destBtn = (Button)findViewById(R.id.dest_add);
		listView = (ListView)findViewById(R.id.list_view);
		
		initData();
		bindEvent();
		
		Intent smsIntent = new Intent(this, SMSService.class);
		startService(smsIntent);
	}
	
	@SuppressWarnings("unchecked")
	private void initData(){
		preferences = QuantaAppUtil.getSharedPreferences(this);
		sqlite = new PhoneSqlite(this);
		ArrayList<HashMap<String, String>> mapList = sqlite.select(null, null);
		try {
			dataList = (ArrayList<SMSMsg>) QuantaAppUtil.hashMapToModel("SMSMsg", mapList);
		} catch (Exception e) {
			dataList = new ArrayList<SMSMsg>();
			e.printStackTrace();
		}
		phoneAdapter = new PhoneAdapter(this, dataList);
		listView.setAdapter(phoneAdapter);
		destEdit.setText(preferences.getString("_dest_number", "15602221486"));
	}
	
	private void bindEvent(){
		originBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String number = originEdit.getText().toString();
				originEdit.setText("");
				if (number.equals("")) {
					number = "95566";
				}
				add(number);
				phoneAdapter.notifyDataSetChanged();
				Intent intent = new Intent(SMSService.ACTION_RECEIVER);
				Bundle bundle = new Bundle();
				bundle.putInt("type", 1);
				intent.putExtras(bundle);
				sendBroadcast(intent);
			}
		});
		
		destBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String number = destEdit.getText().toString();
				setDest(number);
				Intent intent = new Intent(SMSService.ACTION_RECEIVER);
				Bundle bundle = new Bundle();
				bundle.putInt("type", 2);
				intent.putExtras(bundle);
				sendBroadcast(intent);
			}
		});
	}

	private void add(String number){
		if (sqlite.count(SMSMsg.COL_NUMBER + "=?", new String[]{number}) > 1) {
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put(SMSMsg.COL_NUMBER, number);
		sqlite.create(values);
		SMSMsg msg = new SMSMsg(number);
		dataList.add(0, msg);
	}
	
	private void setDest(String number){
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("_dest_number", number);
		editor.commit();
	}
	
	public void delelte(String number){
		sqlite.delete(SMSMsg.COL_NUMBER + "=?", new String[]{number});
		sendBroadcast(new Intent(SMSService.ACTION_RECEIVER));
	}
	


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}



	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	


}
