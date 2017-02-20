package com.roy.simpletodo.com.sony.simpletodo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by roy on 2/18/2017.
 */

public class DbHelper extends SQLiteOpenHelper{


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DbContract.DbEntry.TABLE_NAME + " (" +
                    DbContract.DbEntry.COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
                    DbContract.DbEntry.COLUMN_NAME_TITLE + " TEXT," +
                    DbContract.DbEntry.COLUMN_NAME_PRIORITY + " TEXT," +
                    DbContract.DbEntry.COLUMN_NAME_DUEDATE + " TEXT," +
                    DbContract.DbEntry.COLUMN_NAME_NOTES + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DbContract.DbEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "simpletodo.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
