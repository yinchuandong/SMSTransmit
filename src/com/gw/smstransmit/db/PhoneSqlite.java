package com.gw.smstransmit.db;

import com.gw.smstransmit.model.SMSMsg;
import com.quanta.async.QuantaBaseModel;

import android.content.Context;

public class PhoneSqlite extends BaseSqlite{

	public PhoneSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		// TODO Auto-generated method stub
		return "t_phone";
	}

	@Override
	protected String[] tableColumns() {
		String[] col = new String[]{
				SMSMsg.COL_NUMBER
		};
		return col;
	}

	@Override
	public boolean updateModel(QuantaBaseModel model) {
		// TODO Auto-generated method stub
		
		return false;
	}

}
