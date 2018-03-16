package com.example.dfrank.todo_list.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dfrank on 3/12/18.
 */

public class TodoHelper extends SQLiteOpenHelper {
    public static final int DbVersion=1;
    public static final String DbName = "todo.db";
    public TodoHelper(Context context) {
        super(context, DbName, null, DbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TodoContract.TodoEntry.CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
