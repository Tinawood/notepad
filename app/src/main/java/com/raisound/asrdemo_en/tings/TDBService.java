package com.raisound.asrdemo_en.tings;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.io.File;

public class TDBService extends SQLiteOpenHelper {
	private final static int DATABASE_VERSION = 4;
	private final static String DATABASE_NAME = "Ting.db";
	private SQLiteDatabase db = null;

	/**
	 * �������ݿ��������t_records�� id ,�������� ��title,���⣻ content,���ݣ� record_date
	 * ,��¼���ڣ�remind_time�������ڣ�remind�Ƿ����ѣ�shake�Ƿ��𶯣�ring�Ƿ�����
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE [t_records] ([id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "[content] TEXT,  [record_date] DATE,[remind_time] TIME,[url] blob,[nurl] blob"
				+ "[remind] BOOLEAN,[shake] BOOLEAN,[ring] BOOLEAN)"
				+ ";CREATE INDEX [remind_time_index] ON [t_records] ([remind_time]);"
				+ "CREATE INDEX [record_date_index] ON [t_records] ([record_date]);"
				+ "CREATE INDEX [remind_index] ON [t_records] ([remind])";
		db.execSQL(sql);

	}

	public TDBService(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * ִ��ָ����sql���
	 * 
	 * @param sql
	 * @return
	 */
	public Cursor execSQL(String sql) {
		closeDB();
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		return cursor;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists [t_records]";
		db.execSQL(sql);
		sql = "CREATE TABLE [t_records] ([id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ " [content] TEXT,  [record_date] DATE, [remind_time] TIME, [url] blob,[nurl] blob"
				+ "[remind] BOOLEAN,[shake] BOOLEAN,[ring] BOOLEAN)"
				+ ";CREATE INDEX [remind_time_index] ON [t_records] ([remind_time]);"
				+ "CREATE INDEX [record_date_index] ON [t_records] ([record_date]);"
				+ "CREATE INDEX [remind_index] ON [t_records] ([remind])";
		db.execSQL(sql);

	}

	public void insertRecord(String content, String recordDate) {
		insertRecord(content, recordDate, null ,null,null);
	}

	/**
	 * ����һ����¼
	 * 
	 * @param title
	 *            ����
	 * @param content
	 *            ����
	 * @param recordDate
	 *            ��¼ʱ��
	 * @param remindTime
	 *            ����ʱ��
	 * @param shake
	 *            �Ƿ���
	 * @param ring
	 *            �Ƿ�����
	 */
	public void insertRecord(String content, String recordDate,
			String remindTime,String Url,String NUrl) {
		try {
			String sql = "";
			sql = "insert into t_records(content, record_date,remind_time,url,nurl) values('"
					+ content
					+ "','"
					+ recordDate
					+ "','"
					+ remindTime
					+ "','"
					+ Url
					+ "','"
					+ NUrl
					+ "' );";
			closeDB();
			db = this.getWritableDatabase();
			db.execSQL(sql);
			closeDB();
		} catch (Exception e) {
			Log.d("error", e.getMessage());
		}

	}

	/**
	 * ����ָ����idɾ����¼
	 * 
	 * @param id
	 */
	public void deleteRecord(int id) {
		closeDB();
		String sql = "delete from t_records where id = " + id;
		db = this.getWritableDatabase();
		db.execSQL(sql);
		closeDB();
	}

	public void updateRecord(int id, String content,
			String recordDate, String remindTime,String Url,String NUrl) {
		try {
			String sql = "";

			sql = "update t_records set content='"
					+ content + "' ,record_date='" + recordDate
					+ "' ,remind_time='" + remindTime +  "',url='"+Url+ "',nurl='"+NUrl+ "' where id="
					+ id;
			closeDB();
			db = this.getWritableDatabase();
			db.execSQL(sql);
			closeDB();
		} catch (Exception e) {
			Log.d("updateRecord", e.getMessage());
		}
	}


	// ���idֵ�����Ǹ���¼
	public int getMaxId() {
		closeDB();
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select max(id) from t_records", null);
		cursor.moveToFirst();
		int maxId = cursor.getInt(0);
		closeDB();
		return maxId;

	}

	/**
	 * ��ѯ���еļ�¼
	 * 
	 * @return
	 */
	public Cursor query() {
		closeDB();
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select id as _id, content, record_date from t_records order by id desc", null);
		return cursor;
	}

//	public Cursor query2(String sql,String[] args){
//		db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(sql,args);
//		return cursor;
//	}

	/**
	 * ����ָ����id��ѯ��¼
	 * 
	 * @param id
	 *            ָ���ļ�¼id
	 * @return
	 */
	public Cursor query(int id) {
		closeDB();
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select content,shake,ring,record_date,remind_time,url,nurl from t_records where id="
						+ id, null);
		return cursor;
	}



	// ���û��������Ϣ������null
//	public Remind getRemindMsg() {
//		try {
//			Calendar calendar = Calendar.getInstance();
//			int year = calendar.get(Calendar.YEAR);
//			int month = calendar.get(Calendar.MONTH);
//			int day = calendar.get(Calendar.DATE);
//			int hour = calendar.get(Calendar.HOUR_OF_DAY);
//			int minute = calendar.get(Calendar.MINUTE);
//			int second = 0;
//			String sql = "select content,shake,ring from t_records where record_date='"
//					+ year
//					+ "-"
//					+ month
//					+ "-"
//					+ day
//					+ "' and remind_time='"
//					+ hour
//					+ ":"
//					+ minute
//					+ ":"
//					+ second
//					+ "' and remind='true'";
//			closeDB();
//			db = this.getReadableDatabase();
//			Cursor cursor = db.rawQuery(sql, null);
//			boolean flag = cursor.moveToNext();
//			closeDB();
//			if (flag) {
//				String remindMsg = cursor.getString(0);
//				sql = "update t_records set remind='false', shake='false', ring='false' where record_date='"
//						+ year
//						+ "-"
//						+ month
//						+ "-"
//						+ day
//						+ "' and remind_time='"
//						+ hour
//						+ ":"
//						+ minute
//						+ ":"
//						+ second + "' and remind='true'";
//				db = this.getWritableDatabase();
//				db.execSQL(sql);
//				Remind remind = new Remind();
//				remind.msg = remindMsg;
//				remind.date = calendar.getTime();
//				remind.shake = Boolean.parseBoolean(cursor.getString(1));
//				remind.ring = Boolean.parseBoolean(cursor.getString(2));
//				closeDB();
//				return remind;
//			}
//		} catch (Exception e) {
//			Log.d("getRemindMsg", e.getMessage());
//		}
//		return null;
//	}
//
	/**
	 * �ر����ݿ�
	 */
	public void closeDB() {
		if (db != null) {
			db.close();
		}
		close();
	}
//
//	public Cursor getCursorScrollData(int i, int i1) {
//		db = this.getWritableDatabase();
//		Cursor cursor = db.query("t_records",new String[]{"record_date","id as _id","content"}, null, null, null, null,i+","+i1);
//		return cursor;
//	}
}
