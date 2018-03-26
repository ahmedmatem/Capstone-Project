package com.example.android.pfpnotes.models;

import android.database.Cursor;

import com.example.android.pfpnotes.data.DbContract;

/**
 * Created by ahmed on 24/03/2018.
 */

public class Note {
    private int id;
    private String email;
    private String place;
    private int width;
    private int height;
    private int layers;
    private int copies;
    private String status;
    private double price;
    private String date;

    public Note(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(DbContract.NoteEntry._ID));
        email = cursor.getString(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_EMAIL));
        place = cursor.getString(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_PLACE));
        width = cursor.getInt(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_WIDTH));
        height = cursor.getInt(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_HEIGHT));
        layers = cursor.getInt(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_LAYERS));
        copies = cursor.getInt(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_COPIES));
        status = cursor.getString(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_STATUS));
        price = cursor.getDouble(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_PRICE));
        date = cursor.getString(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_DATE));
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPlace() {
        return place;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLayers() {
        return layers;
    }

    public int getCopies() {
        return copies;
    }

    public String getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }
}
