package com.raisound.asrdemo_en.time;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.raisound.asrdemo_en.R;

/**
 * ��˵���� ����ʱ���¼�����
 * 
 * @author ����: LiuJunGuang
 * @version ����ʱ�䣺2011-12-6 ����8:39:13
 */
public class OnSettingMenuItemClick extends MenuItemClickParent implements
		OnMenuItemClickListener, OnClickListener{
	private TimePicker tpRemindTime;
	private CheckBox cbShake, cbRing;
	private int hour, minute;
	private boolean shake, ring;
	private String remindTime;

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		AlertDialog.Builder builder;

		builder = new AlertDialog.Builder(activity);
		builder.setTitle("��������ʱ��");

		LinearLayout remindSettingLayout = (LinearLayout) activity
				.getLayoutInflater().inflate(R.layout.remindsetting, null);
		tpRemindTime = (TimePicker) remindSettingLayout
				.findViewById(R.id.tpRemindTime);
		cbShake = (CheckBox) remindSettingLayout.findViewById(R.id.cbShake);
		cbRing = (CheckBox) remindSettingLayout.findViewById(R.id.cbRing);
		cbShake.setChecked(shake);
		cbRing.setChecked(ring);

		tpRemindTime.setIs24HourView(true);
		if (remindTime != null) {
			tpRemindTime.setCurrentHour(hour);
			tpRemindTime.setCurrentMinute(minute);
		}
		builder.setView(remindSettingLayout);

		builder.setPositiveButton("ȷ��", this);
		builder.setNegativeButton("ȡ��", null);
		AlertDialog adRemindSetting;
		adRemindSetting = builder.create();
		adRemindSetting.show();
		return true;

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		hour = tpRemindTime.getCurrentHour();
		minute = tpRemindTime.getCurrentMinute();
		remindTime = hour + ":" + minute + ":0";
		shake = cbShake.isChecked();
		ring = cbRing.isChecked();
	}

	public OnSettingMenuItemClick(Activity activity) {
		super(activity);
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public boolean isShake() {
		return shake;
	}

	public boolean isRing() {
		return ring;
	}

	public void setShake(boolean shake) {
		this.shake = shake;
	}

	public void setRing(boolean ring) {
		this.ring = ring;
	}

	public String getRemindTime() {
		return remindTime;
	}
}
