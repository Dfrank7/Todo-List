package com.example.dfrank.todo_list.data;

import android.provider.BaseColumns;

/**
 * Created by dfrank on 3/12/18.
 */

public class TodoContract {
    public static abstract class TodoEntry implements BaseColumns{
        public static final String tableName = "todo";
        public static final String task = "task";
        public static final String priority = "priority";
        public static final String timestamp = "timestamp";

        public static final String CreateTable =  "CREATE TABLE " + tableName + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + task + " TEXT NOT NULL, "
                + priority + " INTEGER NOT NULL DEFAULT 1);";
    }
}
