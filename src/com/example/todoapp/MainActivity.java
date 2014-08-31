package com.example.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String EXTRA_TODO_LIST = "todo_list";

	private static final int REQUEST_TODO_EDIT = 1;

	private final ArrayList<String> mTodoItems = new ArrayList<String>();
	private ArrayAdapter<String> mAdapter;
	private EditText mEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mEditText = (EditText) findViewById(R.id.todo_edit_text);
		ListView listView = (ListView) findViewById(R.id.todo_list_view);
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mTodoItems);
		listView.setAdapter(mAdapter);

		View addButton = findViewById(R.id.add_button);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String todo = mEditText.getText().toString().trim();
				if (!todo.isEmpty()) {
					mTodoItems.add(todo);
					mAdapter.notifyDataSetChanged();
					new SaveTodoTask().execute();
				}
				mEditText.setText("");
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,
						EditTodoItemActivity.class);
				intent.putExtra(EditTodoItemActivity.EXTRA_TODO_ITEM,
						mAdapter.getItem(position));
				intent.putExtra(EditTodoItemActivity.EXTRA_ITEM_POSITION,
						position);
				MainActivity.this.startActivityForResult(intent,
						REQUEST_TODO_EDIT);
			}
		});

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				mTodoItems.remove(position);
				mAdapter.notifyDataSetChanged();
				new SaveTodoTask().execute();
				Toast.makeText(MainActivity.this, R.string.deleted_todo,
						Toast.LENGTH_LONG).show();
				return true;
			}
		});
		
		new ReadTodoTask().execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_TODO_EDIT) {
				final String newTodo = data
						.getStringExtra(EditTodoItemActivity.EXTRA_TODO_ITEM).trim();
				final int position = data.getIntExtra(
						EditTodoItemActivity.EXTRA_ITEM_POSITION, 0);
				if (!mTodoItems.get(position).equals(newTodo)) {
					mTodoItems.set(position, newTodo);
					mAdapter.notifyDataSetChanged();
					new SaveTodoTask().execute();
					Toast.makeText(this, R.string.updated_todo, Toast.LENGTH_LONG)
							.show();
				}
			}
		}
	}
	
	private class SaveTodoTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			final File filesDir = MainActivity.this.getFilesDir();
			final File todoFile = new File(filesDir, "todo.txt");
			try {
				FileUtils.writeLines(todoFile, mTodoItems);
			} catch (IOException ex) {
				Log.e("MainActivity", "Error while writing to todo file", ex);
			}
			return null;
		}
	}
	
	private class ReadTodoTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			final File filesDir = MainActivity.this.getFilesDir();
			final File todoFile = new File(filesDir, "todo.txt");
			try {
				final ArrayList<String> listItems = new ArrayList<String>(FileUtils.readLines(todoFile));
				mTodoItems.clear();
				mTodoItems.addAll(listItems);
				mAdapter.notifyDataSetChanged();
			} catch (IOException ex) {
				Log.e("MainActivity", "Error while reading to todo file", ex);
			}
			return null;
		}
	}
}
