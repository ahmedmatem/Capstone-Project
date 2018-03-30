package com.example.android.pfpnotes.asynctasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.data.Preferences;
import com.example.android.pfpnotes.data.daos.ImageDAO;
import com.example.android.pfpnotes.data.daos.PlaceDAO;
import com.example.android.pfpnotes.data.daos.PriceDAO;
import com.example.android.pfpnotes.interfaces.OnDatabaseListener;
import com.example.android.pfpnotes.models.Dimension;
import com.example.android.pfpnotes.models.NoteModel;

import java.util.ArrayList;

/**
 * Created by ahmed on 19/03/2018.
 */

public class NoteUpdateAsyncTask extends AsyncTask<Context, Void, String> {
    private OnDatabaseListener mListener;
    private NoteModel mNote;

    public NoteUpdateAsyncTask(OnDatabaseListener listener, NoteModel note){
        mListener = listener;
        mNote = note;
    }

    @Override
    protected String doInBackground(Context... contexts) {
        Context context =  contexts[0];
        int numberOfUpdateRows = updateNote(context);
        if (numberOfUpdateRows > 0) {
            updateNoteImages(context);
        }
        return null;
    }

    private int updateNote(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String shortPlaceName = mNote.getShortPlaceName();
        PlaceDAO placeDAO = new PlaceDAO(contentResolver);
        String fullPlaceName = placeDAO.getFullPlaceNameBy(shortPlaceName);
        Dimension dimension = new Dimension(mNote.getDimensionText());

        ContentValues cv = new ContentValues();
        cv.put(DbContract.NoteEntry.COLUMN_PLACE, fullPlaceName);
        cv.put(DbContract.NoteEntry.COLUMN_EMAIL, new Preferences(context).readUserEmail());
        cv.put(DbContract.NoteEntry.COLUMN_WIDTH, dimension.getWidth());
        cv.put(DbContract.NoteEntry.COLUMN_HEIGHT, dimension.getHeight());
        cv.put(DbContract.NoteEntry.COLUMN_LAYERS, dimension.getLayers());
        cv.put(DbContract.NoteEntry.COLUMN_COPIES, dimension.getCopies());
        if(mNote.getStatus() == DbContract.NoteEntry.Status.DONE){
            cv.put(DbContract.NoteEntry.COLUMN_STATUS, DbContract.NoteEntry.Status.UPDATE);
        }
        double price = new PriceDAO(contentResolver)
                .getPriceBySquare(dimension.getSquare());
        cv.put(DbContract.NoteEntry.COLUMN_PRICE,
                price * dimension.getLayers() * dimension.getCopies());

        return contentResolver.update(
                DbContract.NoteEntry.buildContentUriWithId(mNote.getNoteId()),
                cv,
                null,
                null);
    }

    private void updateNoteImages(Context context) {
        ArrayList<String> thumbnails = mNote.getPaths();
        if (thumbnails != null && thumbnails.size() > 0) {
            ContentResolver contentResolver = context.getContentResolver();
            // delete old paths
            new ImageDAO(contentResolver).deleteBy(mNote.getNoteId());
            // add new paths
            boolean isThumbnail = true;
            for (String path : thumbnails) {
                ContentValues cv = new ContentValues();
                cv.put(DbContract.ImageEntry.COLUMN_NOTE_ID, mNote.getNoteId());
                cv.put(DbContract.ImageEntry.COLUMN_IMAGE_PATH, path);
                cv.put(DbContract.ImageEntry.COLUMN_EMAIL,
                        new Preferences(context).readUserEmail());
                cv.put(DbContract.ImageEntry.COLUMN_THUMBNAIL, isThumbnail ? 1 : 0);
                contentResolver.insert(DbContract.ImageEntry.CONTENT_URI, cv);
                if (isThumbnail) {
                    isThumbnail = false;
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if(mListener != null){
            mListener.onDataSaved();
        }
    }
}
