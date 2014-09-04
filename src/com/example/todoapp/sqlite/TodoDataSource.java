package com.example.todoapp.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.example.todoapp.Todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.webkit.WebChromeClient.CustomViewCallback;

public class TodoDataSource {
	public static final String TABLE_NAME = "todo";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_LIST_ID = "list_id";
	public static final String COLUMN_ITEM = "item";
	public static final String COLUMN_DUE_DATE = "due_date";
	public static final String COLUMN_STATUS = "status";
	
	public static final int STATUS_OPEN = 0;
	public static final int STATUS_DONE = 1;
	public static final int STATUS_CANCELED = 2;

	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_LIST_ID + " INTEGER, " 
			+ COLUMN_ITEM + " TEXT NOT NULL, "
			+ COLUMN_DUE_DATE + " INTEGER, " 
			+ COLUMN_STATUS + " INTEGER "
			+ " );";

	private SQLiteDatabase mDatabase;
	private TodoSQLiteHelper dbHelper;

	public TodoDataSource(Context context) {
		dbHelper = new TodoSQLiteHelper(context);
	}

	public void open() throws SQLException {
		mDatabase = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Todo createTodo(int listId, String item, long dueDate) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_ID, listId);
		values.put(COLUMN_ITEM, item);
		values.put(COLUMN_DUE_DATE, dueDate);
		values.put(COLUMN_STATUS, STATUS_OPEN);
		
		long insertId = mDatabase.insert(TABLE_NAME, null, values);
		
		Cursor cursor = mDatabase.query(TABLE_NAME, null, COLUMN_ID + " = " + insertId, null,
		        null, null, null);
		    cursor.moveToFirst();
		    Todo todo = cursorToTodo(cursor);
		    cursor.close();
		    return todo;
	}
	
	public List<Todo> getAllOpenTodos() {
	    List<Todo> todoList = new ArrayList<Todo>();
	    final String selection = COLUMN_STATUS + " = ? ";
	    final String []selectionArgs = new String[] {
	    		String.valueOf(STATUS_OPEN)
	    };
	    
	    Cursor cursor = mDatabase.query(TABLE_NAME, null, selection, selectionArgs , null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Todo todo = cursorToTodo(cursor);
	      todoList.add(todo);
	      cursor.moveToNext();
	    }
	    cursor.close();
	    return todoList;
	  }

	
	private Todo cursorToTodo(Cursor cursor) {
	    Todo todo = new Todo();
	    todo.setId(cursor.getLong(0));
	    todo.setListId(cursor.getInt(1));
	    todo.setItem(cursor.getString(2));
	    todo.setDueDate(cursor.getLong(3));
	    todo.setStatus(cursor.getInt(4));
	    return todo;
	  }
	
	public void update(Todo todo) {
		final String where = COLUMN_ID + " = ? ";
	    final String []whereArgs = new String[] {
	    		String.valueOf(todo.getId())
	    };
	    
	    final ContentValues values = new ContentValues();
		values.put(COLUMN_LIST_ID, todo.getListId());
		values.put(COLUMN_ITEM, todo.getItem());
		values.put(COLUMN_DUE_DATE, todo.getDueDate());
		values.put(COLUMN_STATUS, todo.getStatus());
		
		mDatabase.update(TABLE_NAME, values, where, whereArgs);
	}
}
