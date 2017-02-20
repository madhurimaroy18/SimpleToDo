package com.roy.simpletodo.com.sony.simpletodo.db;

import android.provider.BaseColumns;

/**
 * Created by roy on 2/18/2017.
 */

public final class DbContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DbContract() {}

    /* Inner class that defines the table contents */
    public static class DbEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_DUEDATE = ("date"); //s - a String object in in the format "yyyy-[m]m-[d]d". The leading zero for mm and dd may also be omitted.
        public static final String COLUMN_NAME_NOTES = "notes";

    }
}
