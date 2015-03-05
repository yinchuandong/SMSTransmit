package com.gw.transmit.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.gw.smstransimit.R;
import com.gw.smstransmit.main.MainActivity;
import com.gw.smstransmit.model.SMSMsg;

import android.app.ActionBar.Tab;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class PhoneAdapter extends BaseAdapter {

	class ViewHolder{
		TextView phoneView;
		Button deletBtn;
	}
	private MainActivity activity;
	private ArrayList<SMSMsg> list;
	private LayoutInflater inflater = null;
	
	public PhoneAdapter(Context context, ArrayList<SMSMsg> list) {
		this.activity = (MainActivity)context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listitem, null);
			holder = new ViewHolder();
			holder.deletBtn = (Button)convertView.findViewById(R.id.delete);
			holder.phoneView = (TextView)convertView.findViewById(R.id.phone);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		SMSMsg msg = list.get(position);
		
		holder.phoneView.setText(msg.getPhone());
		bindEvent(position, holder);
		
		return convertView;
	}
	
	private void bindEvent(final int position, ViewHolder holder){
		holder.deletBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String number = list.get(position).getPhone();
				activity.delelte(number);
				list.remove(position);
				notifyDataSetChanged();
			}
		});
	}

}