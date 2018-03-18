package com.example.android.pfpnotes.data.daos;

import android.content.ContentResolver;
import android.database.Cursor;

import com.example.android.pfpnotes.data.DbContract;

/**
 * Created by ahmed on 14/03/2018.
 */

public class PlaceDAO {
    private ContentResolver sContentResolver;

    public PlaceDAO(ContentResolver contentResolver) {
        sContentResolver = contentResolver;
    }

    public String getFullPlaceNameBy(String shortPlaceName) {
        Cursor cursor = sContentResolver.query(DbContract.PlaceEntry.CONTENT_URI,
                null,
                DbContract.PlaceEntry.COLUMN_SHORT_NAME + "=?",
                new String[]{shortPlaceName},
                null);
        if(cursor != null){
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(DbContract.PlaceEntry.COLUMN_NAME));
        }
        return shortPlaceName;
    }

    public String getShortPlaceNameBy(String fullPlaceName){
        Cursor cursor = sContentResolver.query(DbContract.PlaceEntry.CONTENT_URI,
                null,
                DbContract.PlaceEntry.COLUMN_NAME + "=?",
                new String[]{fullPlaceName},
                null);
        if(cursor != null){
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(DbContract.PlaceEntry.COLUMN_SHORT_NAME));
        }
        return fullPlaceName;
    }
}
