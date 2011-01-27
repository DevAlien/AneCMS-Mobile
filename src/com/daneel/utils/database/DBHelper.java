package com.daneel.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "anecms.db";
    private static final String TABLE_NAME = "anecms";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
			db.execSQL("create table " + TABLE_NAME + " ( id integer primary key autoincrement, key text not null, value text not null)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void set(String key, String value) {
        try {
			db.execSQL("INSERT INTO " + TABLE_NAME + "('key', 'value') values ('"
			        + key + "', '"
			        + value + "')");
		} catch (SQLException e) {
			ContentValues cv = new ContentValues();
			cv.put(key, value);
			db.update(TABLE_NAME, cv, "key", new String[] {key});
		}
    }

    public void clearAll() {
        db.delete(TABLE_NAME, null, null);
    }

    public String get(String key) {
        String value = "";
		try {
			Cursor cursor = this.db.query(TABLE_NAME, new String[] {"value"}, "key", new String[] {key}, null, null, null);
			value = "";
			if (cursor.moveToFirst()) {
			    value = cursor.getString(0);
			}
			if (cursor != null && !cursor.isClosed()) {
			    cursor.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return value;
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Here add any steps needed due to version upgrade
        // for example, data format conversions, old tables no longer needed, etc
    }

}