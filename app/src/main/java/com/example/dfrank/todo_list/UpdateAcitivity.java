package com.example.dfrank.todo_list;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.dfrank.todo_list.data.TodoContract;
import com.example.dfrank.todo_list.data.TodoProvider;

import butterknife.BindView;

/**
 * Created by dfrank on 3/19/18.
 */

public class UpdateAcitivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = UpdateAcitivity.class.getSimpleName();

    Uri muri;
    @BindView(R.id.editTextTaskDescription) EditText task;
    @BindView(R.id.radButton1) RadioButton radioButton1;
    @BindView(R.id.radButton2) RadioButton radioButton2;
    @BindView(R.id.radButton3) RadioButton radioButton3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtask);
        Intent intent = getIntent();
        muri = intent.getData();

    }
    private void update(){}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//        switch (item.getItemId()){
//            case R.id.update :
//                update();
//                return true;
//        }
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mTaskData;
            String[] projection ={TodoContract.TodoEntry._ID,
                TodoContract.TodoEntry.task,
                TodoContract.TodoEntry.priority};

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }


            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(muri,
                            projection,
                            null,
                            null,
                            TodoContract.TodoEntry.priority);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
//        String[] projection ={TodoContract.TodoEntry._ID,
//                TodoContract.TodoEntry.task,
//                TodoContract.TodoEntry.priority
//        } ;
//        return new CursorLoader(this, muri, projection, null, null,
//                TodoContract.TodoEntry.priority);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()){
            int taskIndex = cursor.getColumnIndex(TodoContract.TodoEntry.task);
            int priorityIndex = cursor.getColumnIndex(TodoContract.TodoEntry.priority);

            String taskDesc = cursor.getString(taskIndex);

            int priority = cursor.getInt(priorityIndex);

            task.setText(taskDesc);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
