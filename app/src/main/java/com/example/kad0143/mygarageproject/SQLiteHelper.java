package com.example.kad0143.mygarageproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + CarTable.TABLE_NAME + " (" +
                    CarTable._ID + " INTEGER PRIMARY KEY," +
                    CarTable.COLUMN_NAME_BRAND + " TEXT," +
                    CarTable.COLUMN_NAME_MODEL + " INTEGER," +
                    CarTable.COLUMN_NAME_YEAR + " INTEGER," +
                    CarTable.COLUMN_NAME_ENGINE + " INTEGER," +
                    CarTable.COLUMN_NAME_IMAGE + " BLOB)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + "TABLE_NAME";

    // Pokud se změní schéma databáze, musí se změnit verze
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "FeedReader.db";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
