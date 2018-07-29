package com.vlopmartin.apps.organizer;

import android.app.Application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Task {

    public static final String createTableSQL = "CREATE TABLE TASKS (ID INTEGER PRIMARY KEY, NAME TEXT, DESCRIPTION TEXT)";

    private long id;
    private String name;
    private String description;

    public static List<Task> getList(Context ctx) {
        List<Task> ret = new ArrayList<Task>();

        SQLiteDatabase db = new DBHelper(ctx).getReadableDatabase();
        Cursor cursor = db.query("TASKS", null, null, null, null, null, null);

        long taskId;
        String taskName;
        String taskDescription;
        while (cursor.moveToNext()) {
            taskId = cursor.getInt(cursor.getColumnIndex("ID"));
            taskName = cursor.getString(cursor.getColumnIndex("NAME"));
            taskDescription = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
            ret.add(new Task(taskId, taskName, taskDescription));
        }

        return ret;
    }

    public static Task getById(Context ctx, long id) {
        Task ret;

        SQLiteDatabase db = new DBHelper(ctx).getReadableDatabase();
        Cursor cursor = db.query("TASKS", null, "id = ?", new String[] {String.valueOf(id)}, null, null, null);

        if (cursor.moveToNext()) {
            long taskId = cursor.getInt(cursor.getColumnIndex("ID"));
            String taskName = cursor.getString(cursor.getColumnIndex("NAME"));
            String taskDescription = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
            ret = new Task(taskId, taskName, taskDescription);
        } else {
            ret = null;
        }

        return ret;
    }

    public void save(Context ctx) {
        SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NAME", this.name);
        values.put("DESCRIPTION", this.description);

        if (this.id == 0) {
            this.id = db.insert("TASKS", null, values);
        } else {
            db.update("TASKS", values, "ID = ?", new String[] {String.valueOf(this.id)});
        }
    }

    public void delete(Context ctx) {
        SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();

        db.delete("TASKS", "ID = ?", new String[] {String.valueOf(this.id)});
    }

    public Task(long id, String name, String description) {
        setId(id);
        setName(name);
        setDescription(description);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
