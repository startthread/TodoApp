package com.example.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditTodoItemActivity extends Activity {

	public static final String EXTRA_TODO_ITEM = "todo_item";
	public static final String EXTRA_ITEM_POSITION = "todo_item_position";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_todo_item);
		
		final Intent intent = getIntent();
		final int position = intent.getIntExtra(EXTRA_ITEM_POSITION, 0);
		final String todoItem = intent.getStringExtra(EXTRA_TODO_ITEM);
		
		final EditText todoEditText = (EditText) findViewById(R.id.todo_edit_text);
		todoEditText.setText(todoItem);
		todoEditText.setSelection(todoItem.length());
		
		View saveButton = findViewById(R.id.save_button);
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String newTodo = todoEditText.getText().toString();
				final Intent data = new Intent();
				data.putExtra(EXTRA_TODO_ITEM, newTodo);
				data.putExtra(EXTRA_ITEM_POSITION, position);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}
}
