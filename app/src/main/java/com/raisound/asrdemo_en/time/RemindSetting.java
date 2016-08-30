package com.raisound.asrdemo_en.time;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TimePicker;

import com.raisound.asrdemo_en.R;

public class RemindSetting extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remindsetting);
		TimePicker timePicker = (TimePicker) findViewById(R.id.tpRemindTime);
		timePicker.setIs24HourView(true);
	}

}
