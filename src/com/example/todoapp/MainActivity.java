package com.example.todoapp;

import java.util.ArrayList;
import java.util.List;

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
import com.example.todoapp.sqlite.TodoDataSource;

public class MainActivity extends FragmentActivity implements EditItemDialogListener {

	public static final String TAG = "MainActivity";
	
	private final List<Todo> mTodoItems = new ArrayList<Todo>();
	private ArrayAdapter<Todo> mAdapter;
	private EditText mEditText;
	private TodoDataSource mDataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mDataSource = new TodoDataSource(this);
		mDataSource.open();
		
		mEditText = (EditText) findViewById(R.id.todo_edit_text);
		ListView listView = (ListView) findViewById(R.id.todo_list_view);
		mAdapter = new ArrayAdapter<Todo>(this,
				android.R.layout.simple_list_item_1, mTodoItems);
		listView.setAdapter(mAdapter);

		// Add todo item
		View addButton = findViewById(R.id.add_button);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String todo = mEditText.getText().toString().trim();
				if (!todo.isEmpty()) {
					new SaveTodoTask().execute(todo);
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
				final Todo todo = mTodoItems.get(position);
				todo.setStatus(TodoDataSource.STATUS_DONE);
				new UpdateTodoTask().execute(todo);
				
				mTodoItems.remove(position);
				mAdapter.notifyDataSetChanged();
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
			final Todo todo = mTodoItems.get(position);
			todo.setItem(newItem);
			new UpdateTodoTask().execute(todo);
			
			mAdapter.notifyDataSetChanged();
			Toast.makeText(this, R.string.updated_todo, Toast.LENGTH_LONG)
					.show();
		}
	}

	private class SaveTodoTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			try {
				Todo todo = mDataSource.createTodo(0, params[0], System.currentTimeMillis());
				mTodoItems.add(todo);
			} catch (Exception ex) {
				Log.e(TAG, "Error while creating todo", ex);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}
	
	private class UpdateTodoTask extends AsyncTask<Todo, Void, Void> {
		@Override
		protected Void doInBackground(Todo... params) {
			try {
				mDataSource.update(params[0]);
			} catch (Exception ex) {
				Log.e(TAG, "Error while updating todo", ex);
			}
			return null;
		}
	}
	
	private class ReadTodoTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			List<Todo> todoList = mDataSource.getAllOpenTodos();
			mTodoItems.clear();
			mTodoItems.addAll(todoList);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			mAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}

}
