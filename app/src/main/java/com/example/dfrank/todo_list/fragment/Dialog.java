package com.example.dfrank.todo_list.fragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.dfrank.todo_list.MainActivity;
import com.example.dfrank.todo_list.R;
import com.example.dfrank.todo_list.data.TodoContract;
import com.example.dfrank.todo_list.data.TodoProvider;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by dfrank on 3/14/18.
 */

public class Dialog extends DialogFragment {
    @BindView(R.id.editTextTaskDescription) EditText task;
    @BindView(R.id.radButton1) RadioButton radioButton1;
    @BindView(R.id.radButton2) RadioButton radioButton2;
    @BindView(R.id.radButton3) RadioButton radioButton3;
    private int mpriority=1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.addtask,null);
        builder.setTitle("Add Todo");
        setCancelable(false);
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addTask();
                String input = task.getText().toString();
                if (input.isEmpty()) {
                    setCancelable(false);
                    Toast.makeText(getActivity(), "Please Fill the appropriate fields", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put(TodoContract.TodoEntry.task, input);
                    cv.put(TodoContract.TodoEntry.priority, mpriority);
                    Uri uri = getActivity().getContentResolver().insert(TodoProvider.content_provider2, cv);
                    if (uri != null) {
                        Toast.makeText(getActivity(), "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
//                        dialogInterface.dismiss();
                        ((MainActivity) getActivity()).refreshView();
                    }
//                dialogInterface.dismiss();
//                ((MainActivity) getActivity()).refreshView();
                }
            }
        });

        builder.setView(view);
        ButterKnife.bind(this, view);
        return builder.create();


    }

    private void addTask(){
        if(radioButton1.isChecked()){
            mpriority = 1;
        }
        if (radioButton2.isChecked()){
            mpriority = 2;
        }
        if (radioButton3.isChecked()){
            mpriority = 3;
        }
//                insertTodo();

    }


        private void insertTodo(){
        String input = task.getText().toString();
        if (input.length() == 0){
            Toast.makeText(getActivity(), "Please Enter a task", Toast.LENGTH_SHORT).show();
        }
        ContentValues cv = new ContentValues();
        cv.put(TodoContract.TodoEntry.task, input);
        cv.put(TodoContract.TodoEntry.priority, mpriority);
        Uri uri = getActivity().getContentResolver().insert(TodoProvider.content_provider2, cv);
        if (uri!=null){
            Toast.makeText(getActivity(), "Data Inserted Successfully",Toast.LENGTH_SHORT).show();
        }
    }



}
