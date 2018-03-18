package com.example.android.pfpnotes.data.daos;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.models.Dimension;

/**
 * Created by ahmed on 17/03/2018.
 */

public class PriceDAO {
    public static final String INTERVAL_SPLITTER = "-";
    private ContentResolver mContentResolver;

    public PriceDAO(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public double getPriceBySquare(double square) {
        Cursor cursor = mContentResolver.query(DbContract.PriceEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cursor != null) {
            String interval;
            double startBound, endBound;
            while (cursor.moveToNext()) {
                interval = cursor.getString(
                        cursor.getColumnIndex(DbContract.PriceEntry.COLUMN_INTERVAL));
                interval = interval.replace(",", ".");
                String[] splitInterval = interval.split(INTERVAL_SPLITTER);
                startBound = Double.valueOf(splitInterval[0]);
                endBound = Double.valueOf(splitInterval[1]);
                if(startBound <= square && square <= endBound){
                    return cursor.getDouble(
                            cursor.getColumnIndex(DbContract.PriceEntry.COLUMN_PRICE));
                }
            }
        }

        return 0.0;
    }
}
