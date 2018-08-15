package com.vlopmartin.apps.organizer;

import android.app.Application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {

    public static final String createTableSQL = "CREATE TABLE TASKS (ID INTEGER PRIMARY KEY, NAME TEXT, DESCRIPTION TEXT, DUE_DATE INTEGER, PRIORITY INTEGER)";

    private long id;
    private String name;
    private String description;
    private Date dueDate;
    private long priority;


    public static List<Task> getList(Context ctx) {
        List<Task> ret = new ArrayList<Task>();

        SQLiteDatabase db = new DBHelper(ctx).getReadableDatabase();
        Cursor cursor = db.query("TASKS", null, null, null, null, null, null);

        long taskId;
        String taskName;
        String taskDescription;
        long taskDueDate;
        long taskPriority;
        while (cursor.moveToNext()) {
            taskId = cursor.getLong(cursor.getColumnIndex("ID"));
            taskName = cursor.getString(cursor.getColumnIndex("NAME"));
            taskDescription = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
            taskDueDate = cursor.getLong(cursor.getColumnIndex("DUE_DATE"));
            taskPriority = cursor.getLong(cursor.getColumnIndex("PRIORITY"));
            ret.add(new Task(taskId, taskName, taskDescription, taskDueDate == 0 ? null : new Date(taskDueDate), taskPriority));
        }

        cursor.close();
        return ret;
    }

    public static Task getById(Context ctx, long id) {
        Task ret;

        SQLiteDatabase db = new DBHelper(ctx).getReadableDatabase();
        Cursor cursor = db.query("TASKS", null, "id = ?", new String[] {String.valueOf(id)}, null, null, null);

        if (cursor.moveToNext()) {
            long taskId = cursor.getLong(cursor.getColumnIndex("ID"));
            String taskName = cursor.getString(cursor.getColumnIndex("NAME"));
            String taskDescription = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
            long taskDueDate = cursor.getLong(cursor.getColumnIndex("DUE_DATE"));
            long taskPriority = cursor.getLong(cursor.getColumnIndex("PRIORITY"));
            ret = new Task(taskId, taskName, taskDescription, taskDueDate == 0 ? null : new Date(taskDueDate), taskPriority);
        } else {
            ret = null;
        }

        cursor.close();
        return ret;
    }

    public void save(Context ctx) {
        SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();

        ContentValues values = new ContentValues();
        if (this.id != 0) {
            values.put("ID", this.id);
        }
        values.put("NAME", this.name);
        values.put("DESCRIPTION", this.description);
        values.put("DUE_DATE", this.dueDate == null ? 0 : this.dueDate.getTime());
        values.put("PRIORITY", this.priority);

        long id = db.replace("TASKS", null, values);
        this.id = id;
    }

    public void delete(Context ctx) {
        SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();

        db.delete("TASKS", "ID = ?", new String[] {String.valueOf(this.id)});
    }

    public Task(long id, String name, String description, Date dueDate, long priority) {
        setId(id);
        setName(name);
        setDescription(description);
        setDueDate(dueDate);
        setPriority(priority);
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public long getPriority() { return priority; }

    public void setPriority(long priority) { this.priority = priority; }
}
