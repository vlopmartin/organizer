package com.vlopmartin.apps.organizer;

import android.app.Application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Period;
import org.threeten.bp.temporal.ChronoField;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Task {

    private long id;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    private LocalDate dueDate;
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    private int priority;
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    private Period repeatPeriod;
    public Period getRepeatPeriod() { return repeatPeriod; }
    public void setRepeatPeriod(Period repeatPeriod) { this.repeatPeriod = repeatPeriod; }

    private LocalTime notificationTime;
    public LocalTime getNotificationTime() { return notificationTime; }
    public void setNotificationTime(LocalTime notificationTime) { this.notificationTime = notificationTime; }

    private boolean notified;
    public boolean isNotified() { return notified; }
    public void setNotified(boolean notified) { this.notified = notified; }

    public Task(long id, String name, String description, LocalDate dueDate, int priority, Period repeatPeriod, LocalTime notificationTime) {
        setId(id);
        setName(name);
        setDescription(description);
        setDueDate(dueDate);
        setPriority(priority);
        setRepeatPeriod(repeatPeriod);
        setNotificationTime(notificationTime);
        setNotified(false);
    }

    public Task copy() {
        return new Task(0, this.getName(), this.getDescription(), this.getDueDate(), this.getPriority(), this.getRepeatPeriod(), this.getNotificationTime());
    }


    public void save(Context ctx) {
        SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();

        ContentValues values = new ContentValues();
        if (this.id != 0) {
            values.put("ID", this.id);
        }
        values.put("NAME", this.name);
        values.put("DESCRIPTION", this.description);
        values.put("DUE_DATE", this.dueDate == null ? 0 : this.dueDate.getLong(ChronoField.EPOCH_DAY));
        values.put("PRIORITY", this.priority);
        values.put("REPEAT_PERIOD", this.repeatPeriod == null ? "" : this.repeatPeriod.toString());
        values.put("NOTIFICATION_TIME", this.notificationTime == null ? 0 : this.notificationTime.getLong(ChronoField.SECOND_OF_DAY));
        values.put("NOTIFIED", this.notified ? 1 : 0);

        long id = db.replace("TASKS", null, values);
        this.id = id;
    }

    public void delete(Context ctx) {
        SQLiteDatabase db = new DBHelper(ctx).getWritableDatabase();

        db.delete("TASKS", "ID = ?", new String[] {String.valueOf(this.id)});
    }

    public Task repeat(Context ctx) {
        Period period = this.getRepeatPeriod();
        LocalDate dueDate = this.getDueDate();
        if (period != null && dueDate != null) {
            Task task = this.copy();
            LocalDate newDueDate = dueDate.plus(period);
            task.setDueDate(newDueDate);
            task.save(ctx);
            return task;
        } else {
            return null;
        }
    }

    public Task complete(Context ctx) {
        Task task = this.repeat(ctx);
        this.delete(ctx);
        return task;
    }

    private static Task readCursor(Cursor cursor) {
        long taskId = cursor.getLong(cursor.getColumnIndex("ID"));
        String taskName = cursor.getString(cursor.getColumnIndex("NAME"));
        String taskDescription = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
        long taskDueDate = cursor.getLong(cursor.getColumnIndex("DUE_DATE"));
        int taskPriority = cursor.getInt(cursor.getColumnIndex("PRIORITY"));
        String taskRepeatPeriod = cursor.getString(cursor.getColumnIndex("REPEAT_PERIOD"));
        long taskNotificationTime = cursor.getLong(cursor.getColumnIndex("NOTIFICATION_TIME"));
        int taskNotified = cursor.getInt(cursor.getColumnIndex("NOTIFIED"));
        Task task = new Task(
                taskId,
                taskName,
                taskDescription,
                taskDueDate == 0 ? null : LocalDate.ofEpochDay(taskDueDate),
                taskPriority,
                taskRepeatPeriod.equals("") ? null : Period.parse(taskRepeatPeriod),
                taskNotificationTime == 0 ? null : LocalTime.ofSecondOfDay(taskNotificationTime));
        task.setNotified(taskNotified == 1);
        return task;
    }

    public static Task getById(Context ctx, long id) {
        Task ret;

        SQLiteDatabase db = new DBHelper(ctx).getReadableDatabase();
        Cursor cursor = db.query("TASKS", null, "id = ?", new String[] {String.valueOf(id)}, null, null, null);

        if (cursor.moveToNext()) {
            ret = readCursor(cursor);
        } else {
            ret = null;
        }

        cursor.close();
        return ret;
    }

    public static List<Task> getList(Context ctx) {
        List<Task> ret = new ArrayList<Task>();

        SQLiteDatabase db = new DBHelper(ctx).getReadableDatabase();
        Cursor cursor = db.query("TASKS", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            ret.add(readCursor(cursor));
        }

        cursor.close();
        return ret;
    }


    public static class PriorityComparator extends RankComparator<Task> {

        protected int getRank(Task task) {
            int priority = task.getPriority();
            if (priority == 0) return 100;
            else return priority;
        }

    }

    public static class CurrentComparator extends RankComparator<Task> {

        protected int getRank(Task task) {
            LocalDate dueDate = task.getDueDate();
            if (dueDate == null) return 1;
            else {
                if (dueDate.compareTo(LocalDate.now()) <= 0) return 0;
                else return 2;
            }
        }

    }

    public static class DateComparator implements Comparator<Task> {

        @Override
        public int compare(Task t1, Task t2) {
            LocalDate d1 = t1.getDueDate();
            LocalDate d2 = t2.getDueDate();
            if (d1 == null || d2 == null) return 0; // CurrentComparator will take care of nulls!
            return d1.compareTo(d2);
        }

    }
}
