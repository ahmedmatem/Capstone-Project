package com.example.android.pfpnotes.models;

import android.database.Cursor;

import com.example.android.pfpnotes.data.DbContract;

/**
 * Created by ahmed on 24/03/2018.
 */

public class Image {
    private int id;
    private int noteId;
    private String email;
    private String path;

    public Image(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(DbContract.ImageEntry._ID));
        noteId = cursor.getInt(cursor.getColumnIndex(DbContract.ImageEntry.COLUMN_NOTE_ID));
        email = cursor.getString(cursor.getColumnIndex(DbContract.ImageEntry.COLUMN_EMAIL));
        path = cursor.getString(cursor.getColumnIndex(DbContract.ImageEntry.COLUMN_IMAGE_PATH));
    }

    public int getId() {
        return id;
    }

    public int getNoteId() {
        return noteId;
    }

    public String getEmail() {
        return email;
    }

    public String getPath() {
        return path;
    }
}
