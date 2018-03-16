package com.example.dfrank.todo_list.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by dfrank on 3/12/18.
 */

public class TodoProvider extends ContentProvider {

    private static final int Tasks = 100;
    private static final int Task_Id = 101;

    private static final String Content = "content://";
    private static final String Authority = "com.example.dfrank.todo_list";
    private static final String Path_tasks = "todo";
    private static final String Path_task_Id = "todo/#";

    public static final Uri content_provider1 = Uri.parse(Content+Authority+"/"+Path_tasks);
    public static final Uri content_provider2 = Uri.parse(Content+Authority+"/"+Path_task_Id);

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        static {
            uriMatcher.addURI(Authority,Path_tasks,Tasks);
            uriMatcher.addURI(Authority,Path_task_Id,Task_Id);
        }

        private TodoHelper todoHelper;

    @Override
    public boolean onCreate() {
        todoHelper = new TodoHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = null;
        SQLiteDatabase database = todoHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        switch (match){
            case Tasks:
                cursor = database.query(TodoContract.TodoEntry.tableName,
                        strings,s,strings1, null, null,s1);
                break;
            case Task_Id:
                s = TodoContract.TodoEntry._ID +"=?";
                strings1 = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TodoContract.TodoEntry.tableName, strings,s,strings1,
                        null,null,s1);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = uriMatcher.match(uri);
        switch (match){
            case Tasks:
                insertTask(uri, contentValues);
                return (uri);
            default:throw new IllegalArgumentException("insertion is not supported for "+uri);
        }
    }
    private Uri insertTask(Uri uri, ContentValues contentValues){
        Uri returnUri;
        SQLiteDatabase database = todoHelper.getWritableDatabase();
        String description = contentValues.getAsString(TodoContract.TodoEntry.task);
        int priority = contentValues.getAsInteger(TodoContract.TodoEntry.priority);

        long id = database.insert(TodoContract.TodoEntry.tableName, null,contentValues);
        if (id>0){
            returnUri = ContentUris.withAppendedId(uri,id);
        }else {
            throw new android.database.SQLException("Unsupported Uri");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase database = todoHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int id=0;
        switch (match){
            case Tasks:
                id = database.delete(TodoContract.TodoEntry.tableName,s,strings);
                if (id!=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
            case Task_Id:
                s = TodoContract.TodoEntry._ID +"=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                id = database.delete(TodoContract.TodoEntry.tableName, s, strings);
                if (id!=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
        }
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int match = uriMatcher.match(uri);
        switch (match){
            case Task_Id:
                updateTask(uri,contentValues,s,strings);
            default:throw new IllegalArgumentException("update is not supported for "+uri);
        }

    }
    private int updateTask(Uri uri, ContentValues contentValues, String s, String[] strings){
        int id;
        SQLiteDatabase database = todoHelper.getWritableDatabase();
        s = TodoContract.TodoEntry._ID +"=?";
        strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
        String description = contentValues.getAsString(TodoContract.TodoEntry.task);
        int priority = contentValues.getAsInteger(TodoContract.TodoEntry.priority);

        id = database.update(TodoContract.TodoEntry.tableName, contentValues, s,strings);
        if (id>=0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return id;
    }
}
