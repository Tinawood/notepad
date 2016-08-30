package com.raisound.asrdemo_en.time;
import com.raisound.asrdemo_en.time.DBService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CallAlarm extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		DBService dbService = new DBService(context);
		Remind remind = dbService.getRemindMsg();
		dbService.close();// 关闭数据库连接
		Log.d("kuaixiangle", getClass().getSimpleName());
		if (remind != null) {
			Intent myIntent = new Intent(context, AlarmAlert.class);
			Bundle bundleRet = new Bundle();
			bundleRet.putString("remindMsg", remind.msg);
			bundleRet.putBoolean("shake", remind.shake);
			bundleRet.putBoolean("ring", remind.ring);
			myIntent.putExtras(bundleRet);
			myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(myIntent);
		}

	}
	

}
