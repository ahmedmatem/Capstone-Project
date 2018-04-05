package com.example.android.pfpnotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.pfpnotes.data.DbContract.NoteEntry.Status;

/**
 * Created by ahmed on 01/03/2018.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pfp.db";
    private static final int DATABASE_VERSION = 15;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_PRICE_TABLE = "CREATE TABLE " +
                DbContract.PriceEntry.TABLE_NAME + " (" +
                DbContract.PriceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.PriceEntry.COLUMN_START_DATE + " TEXT NOT NULL," +
                DbContract.PriceEntry.COLUMN_INTERVAL + " TEXT NOT NULL," +
                DbContract.PriceEntry.COLUMN_PRICE + " REAL NOT NULL)";

        final String SQL_CREATE_PLACE_TABLE = "CREATE TABLE " +
                DbContract.PlaceEntry.TABLE_NAME + " (" +
                DbContract.PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.PlaceEntry.COLUMN_NAME + " TEXT NOT NULL," +
                DbContract.PlaceEntry.COLUMN_SHORT_NAME + " TEXT NOT NULL)";

        final String SQL_CREATE_NOTE_TABLE = "CREATE TABLE " +
                DbContract.NoteEntry.TABLE_NAME + " (" +
                DbContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.NoteEntry.COLUMN_EMAIL + " TEXT NOT NULL," +
                DbContract.NoteEntry.COLUMN_PLACE + " TEXT NOT NULL," +
                DbContract.NoteEntry.COLUMN_WIDTH + " INTEGER NOT NULL," +
                DbContract.NoteEntry.COLUMN_HEIGHT + " INTEGER NOT NULL," +
                DbContract.NoteEntry.COLUMN_LAYERS + " INTEGER DEFAULT 1," +
                DbContract.NoteEntry.COLUMN_COPIES + " INTEGER DEFAULT 1," +
                DbContract.NoteEntry.COLUMN_PRICE + " REAL DEFAULT 0," +
                DbContract.NoteEntry.COLUMN_DATE + " TEXT NOT NULL," +
                DbContract.NoteEntry.COLUMN_DOCUMENT_ID + " TEXT," +
                DbContract.NoteEntry.COLUMN_STATUS + " INTEGER DEFAULT " +
                Status.UPLOAD + ")";

        final String SQL_CREATE_IMAGE_TABLE = "CREATE TABLE " +
                DbContract.ImageEntry.TABLE_NAME + " (" +
                DbContract.ImageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.ImageEntry.COLUMN_IMAGE_PATH + " TEXT NOT NULL," +
                DbContract.ImageEntry.COLUMN_NOTE_ID + " INTEGER NOT NULL," +
                DbContract.ImageEntry.COLUMN_THUMBNAIL + " INTEGER DEFAULT 0," +
                DbContract.ImageEntry.COLUMN_EMAIL + " TEXT NOT NULL)";

        db.execSQL(SQL_CREATE_PRICE_TABLE);
        db.execSQL(SQL_CREATE_PLACE_TABLE);
        db.execSQL(SQL_CREATE_NOTE_TABLE);
        db.execSQL(SQL_CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.PriceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.PlaceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.NoteEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.ImageEntry.TABLE_NAME);
        onCreate(db);
    }
}
