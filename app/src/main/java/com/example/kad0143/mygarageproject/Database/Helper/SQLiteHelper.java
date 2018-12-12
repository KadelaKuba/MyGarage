package com.example.kad0143.mygarageproject.Database.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kad0143.mygarageproject.Database.Table.CarBasicInformationTable;
import com.example.kad0143.mygarageproject.Database.Table.CarTable;
import com.example.kad0143.mygarageproject.Database.Table.OwnerDataTable;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_CAR_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + CarTable.TABLE_NAME + " (" +
                    CarTable._ID + " INTEGER PRIMARY KEY," +
                    CarTable.COLUMN_NAME_BRAND + " TEXT," +
                    CarTable.COLUMN_NAME_MODEL + " TEXT," +
                    CarTable.COLUMN_NAME_YEAR + " INTEGER," +
                    CarTable.COLUMN_NAME_ENGINE + " TEXT," +
                    CarTable.COLUMN_NAME_IMAGE + " BLOB)";

    private static final String SQL_CREATE_CAR_INFORMATION_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + CarBasicInformationTable.TABLE_NAME + " (" +
                    CarBasicInformationTable._ID + " INTEGER PRIMARY KEY," +
                    CarBasicInformationTable.COLUMN_NAME_CAR_ID + " INTEGER," +
                    CarBasicInformationTable.COLUMN_NAME_TACHOMETER + " INTEGER," +
                    CarBasicInformationTable.COLUMN_NAME_REGISTRATION_DATE + " TEXT," +
                    CarBasicInformationTable.COLUMN_NAME_STK_VALIDITY + " TEXT," +
                    CarBasicInformationTable.COLUMN_NAME_DOCTOR_VALIDITY + " TEXT)";

    private static final String SQL_CREATE_OWNER_DATA_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + OwnerDataTable.TABLE_NAME + " (" +
                    OwnerDataTable._ID + " INTEGER PRIMARY KEY," +
                    OwnerDataTable.COLUMN_NAME_CAR_ID + " INTEGER," +
                    OwnerDataTable.COLUMN_NAME_NAME + " TEXT," +
                    OwnerDataTable.COLUMN_NAME_PHONE + " TEXT," +
                    OwnerDataTable.COLUMN_NAME_STREET + " TEXT," +
                    OwnerDataTable.COLUMN_NAME_CITY + " TEXT," +
                    OwnerDataTable.COLUMN_NAME_POSTCODE + " TEXT)";

    private static final String SQL_DELETE_CAR_ENTRIES = "DROP TABLE IF EXISTS " + CarTable.TABLE_NAME;
    private static final String SQL_DELETE_CAR_INFORMATION_ENTRIES = "DROP TABLE IF EXISTS " + CarBasicInformationTable.TABLE_NAME;
    private static final String SQL_DELETE_OWNER_DATA_ENTRIES = "DROP TABLE IF EXISTS " + OwnerDataTable.TABLE_NAME;

    // Pokud se změní schéma databáze, musí se změnit verze
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "FeedReader.db";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CAR_ENTRIES);
        db.execSQL(SQL_CREATE_CAR_INFORMATION_ENTRIES);
        db.execSQL(SQL_CREATE_OWNER_DATA_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CAR_ENTRIES);
        db.execSQL(SQL_DELETE_CAR_INFORMATION_ENTRIES);
        db.execSQL(SQL_DELETE_OWNER_DATA_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
