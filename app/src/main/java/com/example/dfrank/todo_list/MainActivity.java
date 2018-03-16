package com.example.dfrank.todo_list;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.dfrank.todo_list.adapter.TodoAdapter;
import com.example.dfrank.todo_list.data.TodoContract;
import com.example.dfrank.todo_list.data.TodoHelper;
import com.example.dfrank.todo_list.data.TodoProvider;
import com.example.dfrank.todo_list.fragment.Dialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int TASK_LOADER_ID = 0;
    private TodoAdapter todoAdapter;

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.recyclerViewTasks) RecyclerView recyclerView;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog();
                dialog.show(getFragmentManager().beginTransaction(), null);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new TodoAdapter(this);
        recyclerView.setAdapter(todoAdapter);

        getLoaderManager().initLoader(TASK_LOADER_ID, null, this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();
                String stringId = Integer.toString(id);
                Uri uri = TodoProvider.content_provider2;
                uri = uri.buildUpon().appendPath(stringId).build();

                getContentResolver().delete(uri, null, null);
                getLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this);
            }


        }).attachToRecyclerView(recyclerView);

    }

//    private void Undo(){
//        Snackbar.make(constraintLayout,"Task Deleted",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        })
//    }



    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(TASK_LOADER_ID,null,this);
    }

    public void refreshView(){
        getLoaderManager().restartLoader(TASK_LOADER_ID,null,this);
    }




    private void deleteTask(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to clear all the task");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getContentResolver().delete(TodoProvider.content_provider1,null,null);
                refreshView();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mTaskData;

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
                    return getContentResolver().query(TodoProvider.content_provider1,
                            null,
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

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        todoAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        todoAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                deleteTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


