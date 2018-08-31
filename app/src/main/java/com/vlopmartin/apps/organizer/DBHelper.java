package com.vlopmartin.apps.organizer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "organizer.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TASKS (" +
                "ID INTEGER PRIMARY KEY, " +
                "NAME TEXT, " +
                "DESCRIPTION TEXT, " +
                "DUE_DATE INTEGER, " +
                "PRIORITY INTEGER, " +
                "REPEAT_PERIOD TEXT, " +
                "NOTIFICATION_TIME INTEGER, " +
                "NOTIFIED INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE TASKS (" +
                    "ID INTEGER PRIMARY KEY, " +
                    "NAME TEXT, " +
                    "DESCRIPTION TEXT, " +
                    "DUE_DATE INTEGER, " +
                    "PRIORITY INTEGER, " +
                    "REPEAT_PERIOD TEXT)");
        }
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE TASKS ADD NOTIFICATION_TIME INTEGER");
            db.execSQL("ALTER TABLE TASKS ADD NOTIFIED INTEGER");
            db.execSQL("UPDATE TASKS SET NOTIFICATION_TIME = 0, NOTIFIED = 0");
        }
    }
}
