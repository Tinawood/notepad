package com.raisound.asrdemo_en.speekui;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.raisound.asrdemo_en.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MaActivity extends Activity implements OnClickListener{
	private Button bt_begin;
	private EditText edText;
	private TextView tv_understander;
	private TextView tv2_understander;
	private TextUnderstander mTextUnderstander;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aivity_main);
//		SpeechUtility.createUtility(MaActivity.this, SpeechConstant.APPID+"=57abdf67"+getString(R.string.app_id));
		SpeechUtility.createUtility(MaActivity.this, SpeechConstant.APPID + "=57abdf67");
		bt_begin = (Button) findViewById(R.id.bt_begin);
		bt_begin.setOnClickListener(MaActivity.this);
		edText = (EditText) findViewById(R.id.edText);
		tv_understander = (TextView) findViewById(R.id.tv_understander);
		tv2_understander =(TextView) findViewById(R.id.tv2_understander);
	}
	@Override
	protected void onStart() {
		super.onStart();
		mTextUnderstander = TextUnderstander.createTextUnderstander(MaActivity.this, null);
		//mTextUnderstander.understandText(arg0, arg1)
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.bt_begin){
			if(edText.getText().toString()!=null){
				understanderText(edText.getText().toString());
			}
		}
		
	}
	private void understanderText(String text){
		mTextUnderstander.understandText(text, mTextUnderstanderListener);
		
	}
	private TextUnderstanderListener mTextUnderstanderListener = new TextUnderstanderListener(){

		@Override
		public void onError(SpeechError arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onResult(UnderstanderResult result) {
			String date = JsonParsor.parseUnderstandResult(
					result.getResultString().toString());
			tv_understander.append(date);
			String time = JsonParsor.parseUnderstandResult2(
					result.getResultString().toString());
			tv2_understander.append(time);

		}};

}
