package com.example.todoapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String EXTRA_TODO_LIST = "todo_list";

	private static final int REQUEST_TODO_EDIT = 1;

	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> mTodoList;
	private EditText mEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState != null) {
			mTodoList = savedInstanceState.getStringArrayList(EXTRA_TODO_LIST);
		} else {
			mTodoList = new ArrayList<String>();
		}
		mEditText = (EditText) findViewById(R.id.todo_edit_text);
		ListView listView = (ListView) findViewById(R.id.todo_list_view);
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mTodoList);
		listView.setAdapter(mAdapter);

		View addButton = findViewById(R.id.add_button);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String todo = mEditText.getText().toString().trim();
				if (!todo.isEmpty()) {
					mTodoList.add(todo);
					mAdapter.notifyDataSetChanged();
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
				mTodoList.remove(position);
				mAdapter.notifyDataSetChanged();
				Toast.makeText(MainActivity.this, R.string.deleted_todo,
						Toast.LENGTH_LONG).show();
				return true;
			}
		});
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putStringArrayList(EXTRA_TODO_LIST, mTodoList);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_TODO_EDIT) {
				final String newTodo = data
						.getStringExtra(EditTodoItemActivity.EXTRA_TODO_ITEM);
				final int position = data.getIntExtra(
						EditTodoItemActivity.EXTRA_ITEM_POSITION, 0);
				mTodoList.set(position, newTodo);
				mAdapter.notifyDataSetChanged();
				Toast.makeText(this, R.string.updated_todo, Toast.LENGTH_LONG)
						.show();
			}
		}
	}
}
