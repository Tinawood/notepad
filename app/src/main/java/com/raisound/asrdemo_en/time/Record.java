package com.raisound.asrdemo_en.time;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.raisound.asrdemo_en.R;
import com.raisound.asrdemo_en.speekui.JsonParsor;
import com.raisound.asrdemo_en.ui.AsrDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Record extends Activity {
	private EditText etContent;
	private OnSettingMenuItemClick setTimeClick;
	private OnSetDateMenuItemClick setDateClick;
	private MainActivity myListActivity;
	private boolean edit = false;
	private int id, index;
	private TextView DateText;
	private TextView TimeText;
	private AsrDialog mAsrDialog;
	private Ringtone mRingtone;
	private ImageButton vBt;
	private boolean isRecordingFlag = false;
	private boolean isRunning = false;
	private String mAsrResult;
	private String mAsrError;
	private int mEnergy;
	private static final int ASR_SUCCESS = 0;
	private static final int ASR_FAILED = 1;
	private final int DISSMISS_DIALOG = 2;
	private static final String TAG = "Record";

//	String Date = null;
//	String Time = null;

	private String DateS;
	private String TimeS;
	private TextView time;
	private TextView date;
	private long time1;
	private long time2;
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

	private TextUnderstander mTextUnderstander;


	private void understanderText(String text) {
		mTextUnderstander.understandText(text, mTextUnderstanderListener);

	}

	private TextUnderstanderListener mTextUnderstanderListener = new TextUnderstanderListener() {

		@Override
		public void onError(SpeechError arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onResult(UnderstanderResult result) {

			DateS = JsonParsor.parseUnderstandResult(
					result.getResultString().toString());;

			TimeS = JsonParsor.parseUnderstandResult2(
					result.getResultString().toString());

			Intent intent = getIntent();
			edit = intent.getBooleanExtra("edit", false);
			AlertDialog.Builder dialog = new AlertDialog.Builder(Record.this);
			dialog.setTitle("��ȷ������ʱ��");
			dialog.setMessage(DateS +"     "+ TimeS);
			dialog.setCancelable(false);
			dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (edit) {
						MainActivity.dbService.updateRecord(id,
								etContent.getText().toString(),
								DateS,
								TimeS, true,
								true);
//						MainActivity.recordArray.set(index, etContent.getText().toString());

					} else {
						// ���
						MainActivity.dbService.insertRecord(
								etContent.getText().toString(),
								DateS,
								TimeS,true,
								true);
						MainActivity.arrayAdapter.insert(etContent.getText().toString(), 0);
						MainActivity.idList.add(0, MainActivity.dbService.getMaxId());// ���б������idֵ����������¼
					}
					finish();
				}
			});
			dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			dialog.show();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record);
//		etTitle = (EditText) findViewById(R.id.etTitle);
		etContent = (EditText) findViewById(R.id.etContent);
		time = (TextView) findViewById(R.id.time);
		date = (TextView) findViewById(R.id.date);
		setTimeClick = new OnSettingMenuItemClick(this);// ����ʱ��
		setDateClick = new OnSetDateMenuItemClick(this);// ��������
		SpeechUtility.createUtility(Record.this, SpeechConstant.APPID + "=57abdf67");
		//1.����SpeechRecognizer���󣬵ڶ���������������дʱ��InitListener
		final SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(Record.this, null);
		//2.������д������������ƴ�Ѷ��MSC API�ֲ�(Android)��SpeechConstant��
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
		mIat.setParameter(SpeechConstant.ASR_PTT, "true");

		Intent intent = getIntent();
		edit = intent.getBooleanExtra("edit", false);
		if (edit) {
			id = intent.getIntExtra("id", 0);
			index = intent.getIntExtra("index", -1);
			Cursor cursor = MainActivity.dbService.query(id);
			if (cursor.moveToLast()) {
//				etTitle.setText(cursor.getString(0));
				etContent.setText(cursor.getString(0));
				setTimeClick
						.setShake(Boolean.parseBoolean(cursor.getString(1)));
				setTimeClick.setRing(Boolean.parseBoolean(cursor.getString(2)));
				date.setText(cursor.getString(3));
				time.setText(cursor.getString(4));
			}
		}
		else
		{
			SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
			java.util.Date curTime = new java.util.Date(System.currentTimeMillis());//��ȡ��ǰʱ��
			final String time1 = formatter2.format(curTime);

			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date curDate = new java.util.Date(System.currentTimeMillis());//��ȡ��ǰʱ��
			final String date1 = formatter1.format(curDate);
			date.setText(date1);
			time.setText(time1);
		}

		ImageButton vadd =(ImageButton) findViewById(R.id.vadd);
		vadd.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mIatResults.clear();
						mIat.startListening(mRecoListener);
						if (mAsrDialog == null) {
							mAsrDialog = new AsrDialog(Record.this);
						}
						mAsrDialog.show();
						break;
					case MotionEvent.ACTION_CANCEL:
						mIat.cancel();
						break;
					case MotionEvent.ACTION_UP:
						mIat.stopListening();
						mAsrDialog.dismiss();
						break;
					default:
						break;
				}
				return false;
			}
		});

		vadd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				understanderText(etContent.getText().toString());
			}
		});

	}



		//������ⲿ��
		protected void onStart() {
			super.onStart();
			//�����ı�����������
			mTextUnderstander = TextUnderstander.createTextUnderstander(Record.this, null);
		}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem miSaveRecord = menu.add(0, 1, 1, "���");
		miSaveRecord.setIcon(R.drawable.rrlist1);
		MenuItem miSetDate = menu.add(0, 2, 2, "������������");
		MenuItem miSetTime = menu.add(0, 3, 3, "��������ʱ��");
		miSetTime.setIcon(R.drawable.clock);

		miSetDate.setIcon(R.drawable.calendar_small);// ��������ͼ��
		miSetTime.setOnMenuItemClickListener(setTimeClick);
		miSetDate.setOnMenuItemClickListener(setDateClick);
		return true;
	}

	public void init(){
		Intent intent = getIntent();
		edit = intent.getBooleanExtra("edit", false);
	}

	private void printResult(RecognizerResult results) {
		etContent = (EditText)findViewById(R.id.etContent);
		String text = JsonParsor.parseIatResult(results.getResultString());
		String sn = null;
		// ��ȡjson����е�sn�ֶ�
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}

		etContent.setText(resultBuffer.toString());
		etContent.setSelection(etContent.length());
	}



	private RecognizerListener mRecoListener = new RecognizerListener() {
		//��д����ص��ӿ�(����Json��ʽ������û��ɲμ���¼12.1)��
		//һ������»�ͨ��onResults�ӿڶ�η��ؽ����������ʶ�������Ƕ�ν�����ۼӣ�
		//���ڽ���Json�Ĵ���ɲμ�MscDemo��JsonParser�ࣻ
		//isLast����trueʱ�Ự������
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d("Result:", results.getResultString());
			printResult(results);
			if (isLast) {
				// TODO ���Ľ��
			}
		}

		//�Ự��������ص��ӿ�
		public void onError(SpeechError error) {
//            showTip(error.getPlainDescription(true));
		}

		@Override
		public void onVolumeChanged(int volume, byte[] bytes) {
		}

		//��ʼ¼��
		public void onBeginOfSpeech(){
		}
		//����ֵ0~30

		//����¼��
		public void onEndOfSpeech() {
		}

		//��չ�ýӿ�
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};


}
