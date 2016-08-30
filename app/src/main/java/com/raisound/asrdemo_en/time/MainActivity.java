package com.raisound.asrdemo_en.time;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.raisound.asrdemo_en.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//import com.ideal.studys.AlarmReceiver;
//import com.ideal.studys.NoteActivity;

public  class MainActivity extends Activity {

	public static List<String> recordArray;
 	public static ArrayAdapter<String> arrayAdapter;
	public static List<Integer> idList = new ArrayList<Integer>();
	public static MainActivity myListActivity;
	public static SimpleCursorAdapter adapter;
	public static MediaPlayer mediaPlayer;// 音乐播放器
	public static Vibrator vibrator;
	int index = 0;
	public static DBService dbService = null;;
	public AlarmManager am;
	private int i = 0;
	private int[] lid = new int[100];
	
	
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.list_view);
		final TextView textView = (TextView) findViewById(R.id.itemtext3);
        
        if (dbService == null) {
			dbService = new DBService(this);
		}
		if (am == null) {
			am = (AlarmManager) getSystemService(ALARM_SERVICE);
		}
		if (recordArray == null)
			recordArray = new ArrayList<String>();

		if (arrayAdapter == null)
//			arrayAdapter = new ArrayAdapter<String>(this,
//					android.R.layout.simple_list_item_1, recordArray);
			arrayAdapter = new ArrayAdapter<String>(this,R.layout.entry, R.id.itemtext,recordArray);

		else
			arrayAdapter.clear();

		idList.clear();
		Chaxun();
		// 查询出所有的记录
		Cursor cursor = dbService.query();
		while (cursor.moveToNext()) {
//			arrayAdapter.add(cursor.getString(1));
			idList.add(cursor.getInt(0));
		}
		cursor.close();
		// 设置标题日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		setTitle(sdf.format(calendar.getTime()));// 将标题设置为当前时间
//		listView.setAdapter(arrayAdapter);
		myListActivity = null;
		myListActivity = this;
		try {
			Intent intent = new Intent(myListActivity, CallAlarm.class);
			PendingIntent sender = PendingIntent.getBroadcast(myListActivity,
					0, intent, 0);
			am.setRepeating(AlarmManager.RTC_WAKEUP, 0, 60 * 1000, sender);
//			am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 60 * 1000, sender);
//			am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,0,sender);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ImageButton Js_add = (ImageButton) findViewById(R.id.add);
		Js_add.setOnClickListener(new OnClickListener(){
			@Override
		public void onClick(View v){
				Intent intent = new Intent(MainActivity.this,Record.class);
				startActivity(intent);
			}
		});

		Js_add.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				final int[] lid = new int[100];
				index = 0;
				i = 0;
				ListView listView = (ListView) findViewById(R.id.list_view);

				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						index = position;
						lid[i] = index;
						++i;
					}
				});

				final ImageButton cleBtn = (ImageButton)findViewById(R.id.acancel);
				final ImageButton delBt = (ImageButton) findViewById(R.id.adelete);
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
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView l, View v, int position, long id) {
				// TODO Auto-generated method stub
				startEditRecordActivity(position);
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
				menu.setHeaderTitle("选择操作");
				menu.add(0, 0, 0, "删除");
				menu.add(0, 1, 1, "取消");
				
			}
		});
	}

	public void Chaxun() {
		ListView listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(adapter);
		Cursor cursor2 = dbService.query();
		adapter = new SimpleCursorAdapter(this, R.layout.entry, cursor2, new String[]{"content", "record_date"}, new int[]{R.id.itemtext, R.id.itemtext2});
		cursor2.close();
	}
    
//	class OnDeleteRecordMenuItemClick extends MenuItemClickParent implements
//			OnMenuItemClickListener {
//
//		public OnDeleteRecordMenuItemClick(OnCreateContextMenuListener onCreateContextMenuListener) {
//			super((Activity) onCreateContextMenuListener);
//		}
//
//		@Override
//		public boolean onMenuItemClick(MenuItem item) {
//			recordArray.remove(index);
//			int id = idList.get(index);
//			idList.remove(index);
//			onStart();
//			dbService.deleteRecord(id);
//			return true;
//		}
//
//	}
    
	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 0:// 删除指定记录
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
		Intent intent = new Intent(this, Record.class);
		intent.putExtra("edit", true);
		intent.putExtra("id", idList.get(index));
		intent.putExtra("index", index);
		startActivity(intent);
	}
    
    public void onStart(){
//    	super.onStart();
//    	ListView listView = (ListView) findViewById(R.id.list_view);
//    	listView.setAdapter(arrayAdapter);
		super.onStart();
		Cursor cursor2 = dbService.query();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.entry, cursor2, new String[]{"content", "record_date"}, new int[]{R.id.itemtext, R.id.itemtext2});
		ListView listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView l, View v, int position, long id) {
				// TODO Auto-generated method stub
				startEditRecordActivity(position);
			}
		});
    }



}
