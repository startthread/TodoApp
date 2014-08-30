package com.example.todoapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {

	private static final String EXTRA_TODO_LIST = "todo_list";
	
	private ListView mListview;
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
        mListview = (ListView) findViewById(R.id.todo_list_view);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTodoList);
        mListview.setAdapter(mAdapter);
        
        View addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String todo = mEditText.getText().toString().trim();
				if (!todo.isEmpty()) {
					mAdapter.add(todo);
				}
				mEditText.setText("");
			}
		});
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putStringArrayList(EXTRA_TODO_LIST, mTodoList);
		super.onSaveInstanceState(outState);
	}
}
