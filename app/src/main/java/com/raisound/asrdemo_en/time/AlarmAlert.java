package com.raisound.asrdemo_en.time;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.raisound.asrdemo_en.R;

/**
 * 提示对话框
 * 
 * @author 作者：LiuJunGuang
 * @version 创建时间：2011-12-6 下午5:48:47
 */
public class AlarmAlert extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 提示信息
		String remindMsg = bundle.getString("remindMsg");
		Log.d("xiangle",getClass().getSimpleName());
		if (bundle.getBoolean("ring")) {
			// 播放音乐
			MainActivity.mediaPlayer = MediaPlayer.create(this, R.raw.ring);
			try {
				MainActivity.mediaPlayer.setLooping(true);
				MainActivity.mediaPlayer.prepare();
			} catch (Exception e) {
				setTitle(e.getMessage());
			}
			MainActivity.mediaPlayer.start();// 开始播放
		}
		if (bundle.getBoolean("shake")) {
			MainActivity.vibrator = (Vibrator) getApplication().getSystemService(
					Service.VIBRATOR_SERVICE);
			MainActivity.vibrator.vibrate(new long[] { 1000, 100, 100, 1000 }, -1);
		}
		new AlertDialog.Builder(AlarmAlert.this)
//				.setIcon(R.drawable.clock)
				.setTitle("提醒")
				.setMessage(remindMsg)
				.setPositiveButton("关 闭",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								AlarmAlert.this.finish();
								// 关闭音乐播放器
								if (MainActivity.mediaPlayer != null)
									MainActivity.mediaPlayer.stop();
								if (MainActivity.vibrator != null)
									MainActivity.vibrator.cancel();
							}
						}).show();

	}
}
