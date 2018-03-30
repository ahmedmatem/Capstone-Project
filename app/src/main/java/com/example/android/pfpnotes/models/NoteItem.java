package com.example.android.pfpnotes.models;

import android.database.Cursor;

import com.example.android.pfpnotes.data.DbContract;

/**
 * Created by ahmed on 11/03/2018.
 */

public class NoteItem extends Item {
    private double mPrice;
    private int mId;
    private int mWidth;
    private int mHeight;
    private int mLayers;
    private int mCopies;
    private String mPlace;
    private String mDate;
    private String mPath;
    private int mNoteStatus;

    public NoteItem(Cursor cursor) {
        mId = cursor.getInt(cursor
                .getColumnIndex(DbContract.NoteEntry._ID));
        mPlace = cursor.getString(cursor
                .getColumnIndex(DbContract.NoteEntry.COLUMN_PLACE));
        mWidth = cursor.getInt(cursor
                .getColumnIndex(DbContract.NoteEntry.COLUMN_WIDTH));
        mHeight = cursor.getInt(cursor
                .getColumnIndex(DbContract.NoteEntry.COLUMN_HEIGHT));
        mLayers = cursor.getInt(cursor
                .getColumnIndex(DbContract.NoteEntry.COLUMN_LAYERS));
        mPrice = cursor.getDouble(cursor
                .getColumnIndex(DbContract.NoteEntry.COLUMN_PRICE));
        mDate = cursor.getString(cursor
                .getColumnIndex(DbContract.NoteEntry.COLUMN_DATE));
        mCopies = cursor.getInt(cursor
                .getColumnIndex(DbContract.NoteEntry.COLUMN_COPIES));
        mNoteStatus = cursor.getInt(cursor
                .getColumnIndex(DbContract.NoteEntry.COLUMN_STATUS));

        mPath = cursor.getString(cursor
                .getColumnIndex(DbContract.ImageEntry.COLUMN_IMAGE_PATH));
    }

    public int getId() {
        return mId;
    }

    public String getPlace() {
        return mPlace;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getLayers() {
        return mLayers;
    }

    public double getPrice() {
        return mPrice;
    }

    public String getDate() {
        return mDate;
    }

    public String getDimension() {
        StringBuilder dimens = new StringBuilder(mWidth + Dimension.DELIMITER_SIGN + mHeight);
        if (mLayers > 1) {
            dimens.append(Dimension.DELIMITER_SIGN + mLayers);
        }
        if (mCopies > 1) {
            dimens.append(Dimension.DELIMITER_SIGN + mCopies);
        }
        return dimens.toString();
    }

    public String getPath() {
        return mPath;
    }

    public int getCopies() {
        return mCopies;
    }

    public int getNoteStatus() {
        return mNoteStatus;
    }

    @Override
    public int getType() {
        return Item.TYPE_NOTE;
    }
}
