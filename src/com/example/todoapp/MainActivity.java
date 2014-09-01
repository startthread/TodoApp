package com.example.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.todoapp.EditItemDialogFragment.EditItemDialogListener;

public class MainActivity extends FragmentActivity implements EditItemDialogListener {

	public static final String TAG = "MainActivity";
	
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

		// Add todo item
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

		// Open edit dialog
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EditItemDialogFragment fragment = EditItemDialogFragment.newInstance(position, 
						mAdapter.getItem(position));
				fragment.setListener(MainActivity.this);
				fragment.show(MainActivity.this.getSupportFragmentManager(), "edit_todo_dialog");
			}
		});

		// Delete todo item
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
	public void onEditItemFinished(int position, String newItem) {
		if (!mTodoItems.get(position).equals(newItem)) {
			mTodoItems.set(position, newItem);
			mAdapter.notifyDataSetChanged();
			new SaveTodoTask().execute();
			Toast.makeText(this, R.string.updated_todo, Toast.LENGTH_LONG)
					.show();
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
				Log.e(TAG, "Error while writing to todo file", ex);
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
				Log.e(TAG, "Error while reading to todo file", ex);
			}
			return null;
		}
	}

}
