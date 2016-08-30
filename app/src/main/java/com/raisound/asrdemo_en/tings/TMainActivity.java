package com.raisound.asrdemo_en.tings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.raisound.asrdemo_en.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public  class TMainActivity extends Activity {


	public static List<String> recordArray;
	public static ArrayAdapter<String> arrayAdapter;
	public static SimpleCursorAdapter adapter;
	public static List<Integer> idList = new ArrayList<Integer>();
	private List<String> array = new ArrayList<String>();
	private List<String> selectid = new ArrayList<String>();
	public static TMainActivity myListActivity;
	int index = 0;
	public static TDBService dbService = null;
	private boolean isMulChoice = false;
	;
	public AlarmManager am;
	private SpeechSynthesizer mTts;
	private String text;
	private int i = 0;
//	private int[] lid = new int[100];


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.t_activity_main);
		ListView listView = (ListView) findViewById(R.id.t_list_view);
		ImageButton cancle = (ImageButton)findViewById(R.id.cancel);
		ImageButton delete = (ImageButton)findViewById(R.id.delete);
		final TextView textView3 = (TextView)findViewById(R.id.itemtext3);
		final TextView textView2 = (TextView)findViewById(R.id.itemtext2);
		final TextView textView1 = (TextView)findViewById(R.id.itemtext);
		SpeechUtility.createUtility(TMainActivity.this, SpeechConstant.APPID + "=57abdf67");
		mTts = SpeechSynthesizer.createSynthesizer(this, myInitListener);
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//���÷�����
		mTts.setParameter(SpeechConstant.SPEED, "50");//��������
		mTts.setParameter(SpeechConstant.VOLUME, "80");//������������Χ0~100
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);


		if (dbService == null) {
			dbService = new TDBService(this);
		}
		if (recordArray == null)
			recordArray = new ArrayList<String>();
		if (arrayAdapter == null)
			arrayAdapter = new ArrayAdapter<String>(this, R.layout.entry, R.id.itemtext, recordArray);
		else {
			arrayAdapter.clear();
		}
		idList.clear();
		Chaxun();

//		 ��ѯ�����еļ�¼
		Cursor cursor = dbService.query();
		while (cursor.moveToNext()) {
			idList.add(cursor.getInt(0));
		}
		cursor.close();




		// ���ñ�������
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		setTitle(sdf.format(calendar.getTime()));// ����������Ϊ��ǰʱ��
		myListActivity = null;
		myListActivity = this;

		ImageButton T_add = (ImageButton) findViewById(R.id.t_add);
		T_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TMainActivity.this, TRecord.class);
				startActivity(intent);
			}
		});

		T_add.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
			final int[] lid = new int[100];
				index = 0;
				i = 0;
				final ListView listView = (ListView) findViewById(R.id.t_list_view);

				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						index = position;
						lid[i] = index;
						++i;
					}
				});

				final ImageButton cleBtn = (ImageButton)findViewById(R.id.cancel);
				final ImageButton delBt = (ImageButton) findViewById(R.id.delete);
				delBt.setVisibility(View.VISIBLE);
				delBt.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
							for (i = 0; i <= lid.length - 1; i++) {
								if (lid[i] ==0){

								}
								else {
									int id = idList.get(lid[i]);
									dbService.deleteRecord(id);
								}
							}
							delBt.setVisibility(View.GONE);
							cleBtn.setVisibility(View.GONE);
							onStart();
						}
				});


				cleBtn.setVisibility(View.VISIBLE);
				cleBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						delBt.setVisibility(View.GONE);
						cleBtn.setVisibility(View.GONE);
						onStart();
					}
				});
				return true;
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView l, View v, int position, long id) {
				// TODO Auto-generated method stub
//				startEditRecordActivity(position);
//				int idd = idList.get(index);
//				String text;
//				Cursor cursor = TMainActivity.dbService.query(idd);
//				text = cursor.getString(0);
//				mTts.startSpeaking("text", mSynListener);
				index = position;
				Cursor cursor = TMainActivity.dbService.query(idList.get(index));
				if (cursor.moveToLast()) {
					text = cursor.getString(0);
				}
				mTts.startSpeaking(text, mSynListener);

			}
		});


		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView l, View v,
										   int position, long id) {
				// TODO Auto-generated method stub
				index = position;
				return false;
			}
		});

		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
											ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("ѡ�����");
				menu.add(0, 0, 0, "�༭");
				menu.add(0, 1, 1, "ɾ��");
				menu.add(0, 2, 2, "ȡ��");

			}
		});

	}

	public void Chaxun() {

		ListView listView = (ListView) findViewById(R.id.t_list_view);
		listView.setAdapter(adapter);
		Cursor cursor2 = dbService.query();
		adapter = new SimpleCursorAdapter(this, R.layout.entry, cursor2, new String[]{"content", "record_date"}, new int[]{R.id.itemtext, R.id.itemtext2});
		cursor2.close();
	}


	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case 0:
				startEditRecordActivity(index);
				break;
			case 1:// ɾ��ָ����¼
//			recordArray.remove(index);
				int id = idList.get(index);
				idList.remove(index);
				dbService.deleteRecord(id);
				onStart();
				break;
			default:
				break;
		}
		return super.onContextItemSelected(item);
	}

	public void startEditRecordActivity(int index) {
		Intent intent = new Intent(this, TRecord.class);
		intent.putExtra("edit", true);
		intent.putExtra("id", idList.get(index));
		intent.putExtra("index", index);
		startActivity(intent);
	}

	public void onStart() {
		super.onStart();
		Cursor cursor2 = dbService.query();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.entry, cursor2, new String[]{"content", "record_date"}, new int[]{R.id.itemtext, R.id.itemtext2});
		ListView listView = (ListView) findViewById(R.id.t_list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView l, View v, int position, long id) {
				// TODO Auto-generated method stub
				index = position;
				Cursor cursor = TMainActivity.dbService.query(idList.get(index));
				if (cursor.moveToLast()) {
					text = cursor.getString(0);
				}
				mTts.startSpeaking(text, mSynListener);
			}
		});
	}

	private InitListener myInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d("mySynthesiezer:", "InitListener init() code = " + code);
		}
	};

	private SynthesizerListener mSynListener = new SynthesizerListener() {
		//�Ự�����ص��ӿڣ�û�д���ʱ��errorΪnull
		public void onCompleted(SpeechError error) {
		}

		//������Ȼص�
		//percentΪ�������0~100��beginPosΪ������Ƶ���ı��п�ʼλ�ã�endPos��ʾ������Ƶ���ı��н���λ�ã�infoΪ������Ϣ��
		public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
		}

		//��ʼ����
		public void onSpeakBegin() {
		}

		//��ͣ����
		public void onSpeakPaused() {
		}

		//���Ž��Ȼص�
		//percentΪ���Ž���0~100,beginPosΪ������Ƶ���ı��п�ʼλ�ã�endPos��ʾ������Ƶ���ı��н���λ��.
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
		}

		//�ָ����Żص��ӿ�
		public void onSpeakResumed() {
		}

		//�Ự�¼��ص��ӿ�
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
		}
	};

}
