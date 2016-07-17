package com.codepath.simpletodo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TodoItemDatabase extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "taskDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DBAdapter";
    private static TodoItemDatabase sInstance;

    // Table Names
    private static final String TABLE_TASKS = "tasks";

    // Post Table Columns
    private static final String TASK_ID = "id";
    private static final String TASK_NAME = "name";
    private static final String TASK_PRIORITY = "priority";
    private static final String TASK_DATE = "date";

    private SQLiteDatabase db;

    private TodoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS +
                "(" +
                TASK_ID + " INTEGER PRIMARY KEY, " +
                TASK_NAME + " TEXT, " +
                TASK_PRIORITY + " FLOAT, " +
                TASK_DATE + " LONG" +
                ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    // This method is called when database is upgraded like
    // modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion,
            int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

    public static synchronized TodoItemDatabase getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TodoItemDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    public void addTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(TASK_NAME, task.name);
            if(task.priority != -1) {
                values.put(TASK_PRIORITY, task.priority);
            }
            if(task.dueDate != -1) {
                values.put(TASK_DATE, task.dueDate);
            }
            if(task.taskid != -1) {
                values.put(TASK_ID, task.taskid);
            }

            db.insertOrThrow(TABLE_TASKS, null, values);
            //db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            //db.endTransaction();
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        String TASK_SELECT_QUERY = String.format("SELECT * FROM %s",
                TABLE_TASKS);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASK_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Task newTask = new Task(cursor.getString(cursor.getColumnIndex(TASK_NAME)));
                    newTask.priority = cursor.getInt(cursor.getColumnIndex(TASK_PRIORITY));
                    newTask.dueDate = cursor.getLong(cursor.getColumnIndex(TASK_DATE));
                    newTask.taskid = cursor.getInt(cursor.getColumnIndex(TASK_ID));

                    tasks.add(newTask);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return tasks;
    }
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TASK_NAME, task.name);
        values.put(TASK_DATE, task.dueDate);
        values.put(TASK_PRIORITY, task.priority);

        // Updating profile picture url for user with that userName
        return db.update(TABLE_TASKS, values, TASK_ID + " = ?",
                new String[] { String.valueOf(task.taskid) });
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            db.delete(TABLE_TASKS, TASK_ID + " = ?", new String[] { String.valueOf(task.taskid) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error ");
        } finally {
            db.endTransaction();
        }
    }
}
