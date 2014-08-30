package com.example.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditTodoItemActivity extends Activity {

	public static final String EXTRA_TODO_ITEM = "TODO_ITEM";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_todo_item);
		
		final Intent intent = getIntent();
		final String todoItem = intent.getStringExtra(EXTRA_TODO_ITEM);
		final EditText todoEditText = (EditText) findViewById(R.id.todo_edit_text);
		todoEditText.setText(todoItem);
		
		View saveButton = findViewById(R.id.save_button);
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String newTodo = todoEditText.getText().toString();
				final Intent intent = new Intent();
				//final Bundle data = new Bundle();
				intent.putExtra(EXTRA_TODO_ITEM, newTodo);
				//data.putString(EXTRA_TODO_ITEM, todoItem);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}
