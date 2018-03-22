package com.example.android.pfpnotes.data.daos;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.android.pfpnotes.data.DbContract;

import java.util.ArrayList;

/**
 * Created by ahmed on 18/03/2018.
 */

public class ImageDAO {
    private ContentResolver mContentResolver;

    public ImageDAO(ContentResolver COntentResolver) {
        mContentResolver = COntentResolver;
    }

    public ArrayList<String> getPaths(int noteId) {
        ArrayList<String> thumbnails = null;
        Cursor cursor = mContentResolver.query(DbContract.ImageEntry.CONTENT_URI,
                new String[]{DbContract.ImageEntry.COLUMN_IMAGE_PATH},
                DbContract.ImageEntry.COLUMN_NOTE_ID + "=?",
                new String[]{String.valueOf(noteId)},
                null);
        if (cursor != null) {
            thumbnails = new ArrayList<>();
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(
                        DbContract.ImageEntry.COLUMN_IMAGE_PATH));
                thumbnails.add(path);
            }
        }
        return thumbnails;
    }

    public int deleteBy(int noteId) {
        return mContentResolver.delete(
                DbContract.ImageEntry.CONTENT_URI,
                DbContract.ImageEntry.COLUMN_NOTE_ID + "=?",
                new String[]{String.valueOf(noteId)});
    }

    public int deleteBy(String path) {
        return mContentResolver.delete(
                DbContract.ImageEntry.CONTENT_URI,
                DbContract.ImageEntry.COLUMN_IMAGE_PATH + "=?",
                new String[]{path});
    }

    public Cursor getPhotoBy(String path) {
        return mContentResolver.query(
                DbContract.ImageEntry.CONTENT_URI,
                null,
                DbContract.ImageEntry.COLUMN_IMAGE_PATH + "=?",
                new String[]{path},
                null);
    }

    public Cursor getNoteImages(int noteId) {
        return mContentResolver.query(
                DbContract.ImageEntry.CONTENT_URI,
                null,
                DbContract.ImageEntry.COLUMN_NOTE_ID + "=?",
                new String[]{String.valueOf(noteId)},
                null);
    }

    public void updateToThumbnail(String path) {
        ContentValues cv = new ContentValues();
        cv.put(DbContract.ImageEntry.COLUMN_THUMBNAIL, 1);
        mContentResolver.update(
                DbContract.ImageEntry.CONTENT_URI,
                cv,
                DbContract.ImageEntry.COLUMN_IMAGE_PATH + "=?",
                new String[]{path});
    }
}
