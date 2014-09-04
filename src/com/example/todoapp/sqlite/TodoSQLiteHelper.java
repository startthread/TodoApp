package com.example.todoapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoSQLiteHelper extends SQLiteOpenHelper {

	public static final String TAG = "TodoSQLiteHelper";
	
	private static final String DATABASE_NAME = "todo.db";
	private static final int DATABASE_VERSION = 1;
	
	private SQLiteDatabase mDatabase;
	
	public TodoSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		mDatabase = db;
		mDatabase.execSQL(TodoDataSource.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		mDatabase = db;
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
		            + newVersion);
		// handle upgrades
	}
}
