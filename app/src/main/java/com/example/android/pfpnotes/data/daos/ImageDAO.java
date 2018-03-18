package com.example.android.pfpnotes.data.daos;

import android.content.ContentResolver;
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

    public ArrayList<String> getPaths(int noteId){
        ArrayList<String> paths = null;
        Cursor cursor = mContentResolver.query(DbContract.ImageEntry.CONTENT_URI,
                new String[]{DbContract.ImageEntry.COLUMN_IMAGE_PATH},
                DbContract.ImageEntry.COLUMN_NOTE_ID + "=?",
                new String[]{String.valueOf(noteId)},
                null);
        if(cursor != null){
            paths = new ArrayList<>();
            while (cursor.moveToNext()){
                paths.add(cursor.getString(cursor.getColumnIndex(
                        DbContract.ImageEntry.COLUMN_IMAGE_PATH)));
            }
        }
        return paths;
    }

    public int deleteBy(int noteId){
        return mContentResolver.delete(
                DbContract.ImageEntry.CONTENT_URI,
                DbContract.ImageEntry.COLUMN_NOTE_ID + "=?",
                new String[]{String.valueOf(noteId)});
    }
}
