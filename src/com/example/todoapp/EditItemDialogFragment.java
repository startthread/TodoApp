package com.example.todoapp;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

public class EditItemDialogFragment extends DialogFragment {
	public static final String ARG_POSITION = "position";
	public static final String ARG_ITEM = "item";
	
	private EditItemDialogListener mListener;
	
	public EditItemDialogFragment() {
	}
	
	public void setListener(EditItemDialogListener listener) {
		this.mListener = listener;
	}

	public static EditItemDialogFragment newInstance(int position, Todo todo) {
		EditItemDialogFragment dialog = new EditItemDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(ARG_POSITION, position);
		bundle.putString(ARG_ITEM, todo.getItem());
		dialog.setArguments(bundle);
		return dialog;
	}
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final String todoItem = getArguments().getString(ARG_ITEM);
		final int position = getArguments().getInt(ARG_POSITION);
		
		AlertDialog.Builder builder = new Builder(getActivity());
		final EditText editText = new EditText(getActivity());
		editText.setText(todoItem);
		editText.setSelection(todoItem.length());
		builder.setView(editText);
		
		builder.setTitle(R.string.edit_todo_dialog_title)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (mListener != null) {
							final String newItem = editText.getText().toString().trim();
							mListener.onEditItemFinished(position, newItem);
						}
					}
				})
				.setNegativeButton(android.R.string.cancel, null);
		
		return builder.create();
	}
	
	public interface EditItemDialogListener {
		void onEditItemFinished(int position, String newItem);
	}
}
