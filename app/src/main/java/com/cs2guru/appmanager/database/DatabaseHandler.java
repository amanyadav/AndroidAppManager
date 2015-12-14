package com.cs2guru.appmanager.database;

import java.util.ArrayList;
import java.util.List;

import com.cs2guru.appmanager.interfaces.DataBaseChangeObserver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static DatabaseHandler mInstance = null;

	static List<DataBaseChangeObserver> observers = null;
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "appsManager";

	// table name
	private static final String TABLE_APP_INFO = "app_info";
	int _id;
	String _name;
	String _pkg_name;
	int _prev_ver;
	int _curr_ver;
	String _unistalled;
	String _icon;

	// Apps Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PKG_NAME = "pkg_name";
	private static final String KEY_COUNT = "count";
	private static final String KEY_DATE = "date";
	private static final String KEY_IMAGE = "image";

	// Single instance
	public static DatabaseHandler getInstance(Context ctx) {

		if (mInstance == null) {
			mInstance = new DatabaseHandler(ctx.getApplicationContext());
		}
		return mInstance;
	}

	// ObserverPattern
	public static void registerObserver(DataBaseChangeObserver observer) {
		if (observers == null) {
			observers = new ArrayList<DataBaseChangeObserver>();
			observers.add(observer);
		} else
			observers.add(observer);
	}

	private void dataChanged() {
		Thread t = new Thread(new Runnable() {
			// this is a little hack so that update is dalayed by some time
			// if this not used then update is called before list is updated in
			// db. Can be better way to do this
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (observers != null) {
					for (DataBaseChangeObserver o : observers) {
						o.dataChanged();
					}
				}

			}
		});
		t.start();

	}

	private DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_APP_INFO
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
				+ " TEXT," + KEY_PKG_NAME + " TEXT," + KEY_COUNT
				+ " INTEGER," + KEY_DATE + " TEXT ," + KEY_IMAGE
				+ " BLOB " + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_INFO);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new App
	public void addAppInfo(AppManagerInfo info) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, info.getName());
		values.put(KEY_PKG_NAME, info.getPackageName());
		values.put(KEY_COUNT, info.getCount());
		values.put(KEY_DATE, info.getDate());
		values.put(KEY_IMAGE, info.getImage());

		// Inserting Row
		db.insert(TABLE_APP_INFO, null, values);
		dataChanged();
	}

	// Getting single app info
	public AppManagerInfo getAppInfo(String pkgName) {
		AppManagerInfo info = null;
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_APP_INFO, new String[] { KEY_ID,
				KEY_NAME, KEY_PKG_NAME, KEY_COUNT, KEY_DATE, KEY_IMAGE },
				KEY_PKG_NAME + "=?", new String[] { pkgName }, null, null,
				null, null);
		if (cursor != null && cursor.moveToFirst()) {
			info = new AppManagerInfo(
					Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), cursor.getString(2),
					Integer.parseInt(cursor.getString(3)),
					cursor.getString(4), cursor.getBlob(5));
		}
		cursor.close();

		// return app
		return info;
	}

	// Getting All Apps
	public List<AppManagerInfo> getAllAppInfo() {
		List<AppManagerInfo> appList = new ArrayList<AppManagerInfo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_APP_INFO;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				AppManagerInfo info = new AppManagerInfo();
				info.setID(Integer.parseInt(cursor.getString(0)));
				info.setName(cursor.getString(1));
				info.setPackageName(cursor.getString(2));
				info.setCount(Integer.parseInt(cursor.getString(3)));
				info.setDate(cursor.getString(4));
				info.setImage(cursor.getBlob(5));

				appList.add(info);
			} while (cursor.moveToNext());
		}
		cursor.close();

		// return appinfo list
		return appList;
	}

	// Updating single app info
	public int updateAppInfo(AppManagerInfo info) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, info.getName());
		values.put(KEY_COUNT, info.getCount());
		values.put(KEY_DATE, info.getDate());
		values.put(KEY_PKG_NAME, info.getPackageName());
		values.put(KEY_IMAGE, info.getImage());

		dataChanged();
		// updating row
		return db.update(TABLE_APP_INFO, values, KEY_ID + " = ?",
				new String[] { String.valueOf(info.getID()) });
	}
}
