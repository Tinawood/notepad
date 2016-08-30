package com.raisound.asrdemo_en.tings;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.raisound.asrdemo_en.R;
import com.raisound.asrdemo_en.speekui.JsonParsor;
import com.raisound.asrdemo_en.time.Record;
import com.raisound.asrdemo_en.ui.AsrDialog;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TRecord extends Activity {
	private static final String TAG = "TRecord";
	private EditText etContent;
	private boolean edit = false;
	private int id, index;
	private AsrDialog mAsrDialog;
	private TextView time;
	private TextView date;
	private Uri imageUri;
	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	private ImageView p;
	private File outputImage;
	private Bitmap bitmap;
    private String bic;
	private String nbic;

	private SpeechSynthesizer mTts;
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.t_record);
//		etTitle = (EditText) findViewById(R.id.etTitle);
		etContent = (EditText) findViewById(R.id.t_etContent);
		time = (TextView) findViewById(R.id.time);
		date = (TextView) findViewById(R.id.date);
		SpeechUtility.createUtility(TRecord.this, SpeechConstant.APPID + "=57abdf67");
		//1.����SpeechRecognizer���󣬵ڶ���������������дʱ��InitListener
		final SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(TRecord.this, null);
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
			Cursor cursor = TMainActivity.dbService.query(id);
			if (cursor.moveToLast()) {
					etContent.setText(cursor.getString(0));
					date.setText(cursor.getString(3));
					time.setText(cursor.getString(4));
					byte[] decode = Base64.decode(cursor.getString(5), Base64.DEFAULT);
					Bitmap bmp = BitmapFactory.decodeByteArray(decode, 0, decode.length);
					saveBitmap(bmp);
			}
		} else {
			SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
			Date curTime = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
			final String time1 = formatter2.format(curTime);

			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy��MM��dd��");
			Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
			final String date1 = formatter1.format(curDate);
			date.setText(date1);
			time.setText(time1);
		}


		SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
		Date curTime = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
		final String time1 = formatter2.format(curTime);

		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy��MM��dd��");
		Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
		final String date1 = formatter1.format(curDate);

		ImageButton tp = (ImageButton) findViewById(R.id.tp);
		p = (ImageView) findViewById(R.id.photo);
		ImageButton v_add = (ImageButton) findViewById(R.id.v_add);
		v_add.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mIatResults.clear();
						mIat.startListening(mRecoListener);
						if (mAsrDialog == null) {
							mAsrDialog = new AsrDialog(TRecord.this);
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


		v_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edit) {
					TMainActivity.dbService.updateRecord(id,
							etContent.getText().toString(),
							date1,
							time1,
							bic,
							nbic);
				} else {
					// ���
					TMainActivity.dbService.insertRecord(
							etContent.getText().toString(),
							date1,
							time1,
							bic,
							nbic);
					TMainActivity.arrayAdapter.insert(etContent.getText().toString(), 0);
					TMainActivity.idList.add(0, TMainActivity.dbService.getMaxId());// ���б������idֵ����������¼
				}

				finish();
			}
		});

		tp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				outputImage = new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
				try{
					if(outputImage.exists()){
						outputImage.delete();
					}
					outputImage.createNewFile();
				}catch (IOException e){
					e.printStackTrace();

				}
				imageUri = Uri.fromFile(outputImage);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, TAKE_PHOTO);

			}
		});
	}

	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		switch (requestCode){
			case TAKE_PHOTO:
				if(resultCode == RESULT_OK){
					Intent intent = new Intent("com.android.camera.action.CROP");
					intent.setDataAndType(imageUri,"image/*");
					intent.putExtra("scale", true);
					intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
					startActivityForResult(intent ,CROP_PHOTO);
				}
				break;
			case CROP_PHOTO:
				if(resultCode == RESULT_OK)
					try{
//						EditText et2Content = (EditText)findViewById(R.id.t_etContent);
						bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
						p.setImageBitmap(bitmap);
						p.setVisibility(View.VISIBLE);
//						SpannableString spannable = new SpannableString(etContent.getText().toString() + "[smile]");
//						ImageSpan span = new ImageSpan(this,bitmap);
//						spannable.setSpan(span, etContent.getText().length(), etContent.getText().length() + "[smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//						etContent.setText(spannable);
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);//ѹ��ͼƬ
						byte[] b = stream.toByteArray();
						// ��ͼƬ�����ַ�����ʽ�洢����
//						String tp = new String(Base64coder.encodeLines(b));
						bic = Base64.encodeToString(b,Base64.DEFAULT);
					}catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
				break;

		}
	}

	private void printResult(RecognizerResult results) {
		etContent = (EditText)findViewById(R.id.t_etContent);
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

	private void saveBitmap(Bitmap bitmap) {
		try {
//			EditText et2Content = (EditText)findViewById(R.id.t_etContent);
			String path = Environment.getExternalStorageDirectory().getPath()
					+"/decodeImage.jpg";
			Log.d("linc", "path is " + path);
			OutputStream stream = new FileOutputStream(path);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
			stream.close();
			Log.e("linc", "jpg okay!");
			ImageView iv = (ImageView)findViewById(R.id.photo);
//			SpannableString spannable = new SpannableString(etContent.getText().toString() + "[smile]");
//			ImageSpan span = new ImageSpan(this,bitmap);
//			spannable.setSpan(span, etContent.getText().length(), etContent.getText().length() + "[smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//			et2Content.setText(spannable);
			iv.setVisibility(View.VISIBLE);
			iv.setImageBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("linc","failed: "+e.getMessage());
		}
	}

}
