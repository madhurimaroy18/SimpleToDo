package com.roy.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.roy.simpletodo.com.roy.simpletodo.utils.DateTime;
import com.roy.simpletodo.com.sony.simpletodo.db.DbContract;
import com.roy.simpletodo.com.sony.simpletodo.db.DbHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by roy on 2/18/2017.
 *
 * Reference: https://developer.android.com/training/basics/data-storage/databases.html
 */

class DbActivity {

    Context ctx;

    public DbActivity(Context c){
        this.ctx = c;
    }

    public ArrayList<Item> readAllFromDb() {
        ArrayList<Item> items = new ArrayList<>();
        DbHelper mDbHelper = new DbHelper(this.ctx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
   //     mDbHelper.onUpgrade(db, 1, 2);

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                DbContract.DbEntry.COLUMN_NAME_ID,
                DbContract.DbEntry.COLUMN_NAME_TITLE,
                DbContract.DbEntry.COLUMN_NAME_PRIORITY,
                DbContract.DbEntry.COLUMN_NAME_DUEDATE,
                DbContract.DbEntry.COLUMN_NAME_NOTES
        };

// Filter results WHERE "title" = 'My Title'
        String selection = DbContract.DbEntry._ID + " = ?";
        String[] selectionArgs = { "rownum" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                DbContract.DbEntry.COLUMN_NAME_ID + " ASC";     //show oldest items top

        Cursor cursor = db.query(
                DbContract.DbEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        while(cursor.moveToNext()) {

            //      long rowid = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.DbEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DbEntry.COLUMN_NAME_TITLE));
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DbEntry.COLUMN_NAME_ID));
            String pr = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DbEntry.COLUMN_NAME_PRIORITY));
            String strD = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DbEntry.COLUMN_NAME_DUEDATE));
            Date d = DateTime.stringToDate(strD, "yyyy-MM-dd");
            String n = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.DbEntry.COLUMN_NAME_NOTES));

            Item i = new Item(id, name, Item.PRIORITY.valueOf(pr), d, n);
            items.add(i);
        }
        cursor.close();
        db.close();

        return items;
    }

    public boolean writeToDb(Item it){

        boolean result = true;
        DbHelper mDbHelper = new DbHelper(this.ctx);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//  a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DbContract.DbEntry.COLUMN_NAME_ID, it.getId());
        values.put(DbContract.DbEntry.COLUMN_NAME_TITLE, it.getName());
        values.put(DbContract.DbEntry.COLUMN_NAME_PRIORITY, it.getPriority().toString());
        String fdate = DateTime.dateToString(it.getDate(), "yyyy-MM-dd");
        values.put(DbContract.DbEntry.COLUMN_NAME_DUEDATE, fdate);
        values.put(DbContract.DbEntry.COLUMN_NAME_NOTES, it.getNotes());

// Insert the new row, returning the primary key value of the new row
        try {
            long newRowId = db.insert(DbContract.DbEntry.TABLE_NAME, null, values);
        }catch(SQLiteException e){
            result = false;
            Log.d("SQLiteException", e.toString());
        }
        db.close();
        return result;
    }

    public boolean writeToDb(ArrayList<Item> items){
        boolean result = true;
        for (Item i : items){
            result = writeToDb(i);
        }
        return result;
    }

    public boolean deleteInDb(String id) {
        boolean result = true;

        // Define 'where' part of query.
        DbHelper mDbHelper = new DbHelper(this.ctx);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = DbContract.DbEntry.COLUMN_NAME_ID + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = { id };
// Issue SQL statement.
        try{
            db.delete(DbContract.DbEntry.TABLE_NAME, selection, selectionArgs);
        }catch(SQLiteException e){
            result = false;
            Log.v("SQLiteException", e.toString());
        }
        db.close();
        return result;
    }

    boolean updateInDb(Item newi, String oldId){
        boolean result = true;

        DbHelper mDbHelper = new DbHelper(this.ctx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(DbContract.DbEntry.COLUMN_NAME_TITLE, newi.getName());
        values.put(DbContract.DbEntry.COLUMN_NAME_PRIORITY, newi.getPriority().toString());
        String fdate = DateTime.dateToString(newi.getDate(), "yyyy-MM-dd");
        values.put(DbContract.DbEntry.COLUMN_NAME_DUEDATE, fdate);
        values.put(DbContract.DbEntry.COLUMN_NAME_NOTES, newi.getNotes());


// Which row to update, based on the title
        String selection = DbContract.DbEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = {oldId};

        try {
            int count = db.update(
                    DbContract.DbEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            if(count == 0){
                result = false;
            }
        }catch(SQLiteException e){
            result = false;
            Log.v("SQLiteException", e.toString());
        }
        db.close();
        return result;
    }
}
