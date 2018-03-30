package com.example.android.pfpnotes.asynctasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.pfpnotes.data.DateHelper;
import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.data.Preferences;
import com.example.android.pfpnotes.data.daos.PlaceDAO;
import com.example.android.pfpnotes.data.daos.PriceDAO;
import com.example.android.pfpnotes.interfaces.OnDatabaseListener;
import com.example.android.pfpnotes.models.Dimension;
import com.example.android.pfpnotes.models.NoteModel;

/**
 * Created by ahmed on 20/03/2018.
 */

public class NoteAddAsyncTask extends AsyncTask<Context, Void, Void> {
    private NoteModel mNote;
    private OnDatabaseListener mListener;

    public NoteAddAsyncTask(NoteModel note, OnDatabaseListener listener) {
        mNote = note;
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        Context context = contexts[0];
        Uri uri = insertNote(context);
        if(uri != null) {
            insertNoteImages(context, uri);
        }
        return null;
    }

    private Uri insertNote(Context context) {
        if(mNote == null){
            return null;
        }
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = null;
        PlaceDAO placeDAO = new PlaceDAO(contentResolver);
        String fullPlaceName = placeDAO.getFullPlaceNameBy(mNote.getShortPlaceName());
        Dimension dimension = new Dimension(mNote.getDimensionText());

        ContentValues cv = new ContentValues();
        cv.put(DbContract.NoteEntry.COLUMN_PLACE, fullPlaceName);
        cv.put(DbContract.NoteEntry.COLUMN_EMAIL, new Preferences(context).readUserEmail());
        cv.put(DbContract.NoteEntry.COLUMN_WIDTH, dimension.getWidth());
        cv.put(DbContract.NoteEntry.COLUMN_HEIGHT, dimension.getHeight());
        cv.put(DbContract.NoteEntry.COLUMN_LAYERS, dimension.getLayers());
        cv.put(DbContract.NoteEntry.COLUMN_COPIES, dimension.getCopies());
        cv.put(DbContract.NoteEntry.COLUMN_DATE, DateHelper.getCurrentDate());
        double price = new PriceDAO(contentResolver)
                .getPriceBySquare(dimension.getSquare());
        cv.put(DbContract.NoteEntry.COLUMN_PRICE,
                price * dimension.getLayers() * dimension.getCopies());
        cv.put(DbContract.NoteEntry.COLUMN_STATUS, DbContract.NoteEntry.Status.UPLOAD);

        return contentResolver.insert(DbContract.NoteEntry.CONTENT_URI, cv);
    }

    private void insertNoteImages(Context context, Uri uri) {
        if (uri != null && mNote != null) {
            if (mNote.getPaths() != null && mNote.getPaths().size() > 0) {
                boolean isThumbnail = true;
                int noteId = Integer.valueOf(uri.getLastPathSegment());
                ContentResolver contentResolver = context.getContentResolver();
                for (String path : mNote.getPaths()) {
                    ContentValues cv = new ContentValues();
                    cv.put(DbContract.ImageEntry.COLUMN_NOTE_ID, noteId);
                    cv.put(DbContract.ImageEntry.COLUMN_IMAGE_PATH, path);
                    cv.put(DbContract.ImageEntry.COLUMN_EMAIL,
                            new Preferences(context).readUserEmail());
                    cv.put(DbContract.ImageEntry.COLUMN_THUMBNAIL, isThumbnail ? 1 : 0);
                    contentResolver.insert(DbContract.ImageEntry.CONTENT_URI, cv);
                    if(isThumbnail) {
                        isThumbnail = false;
                    }
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(mListener != null){
            mListener.onDataSaved();
        }
    }
}
