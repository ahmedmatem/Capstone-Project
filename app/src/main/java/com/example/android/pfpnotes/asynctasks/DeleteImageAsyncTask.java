package com.example.android.pfpnotes.asynctasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.data.daos.ImageDAO;
import com.example.android.pfpnotes.interfaces.OnDatabaseListener;
import com.example.android.pfpnotes.models.NoteModel;

import java.io.File;

/**
 * Created by ahmed on 22/03/2018.
 */

public class DeleteImageAsyncTask extends AsyncTask<Object, Void, Void> {
    private OnDatabaseListener mListener;
    private Context mContext;

    public DeleteImageAsyncTask(Context context) {
        mContext = context;
        try {
            mListener = (OnDatabaseListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnDatabaseListener");
        }
    }

    @Override
    protected Void doInBackground(Object... objects) {
        NoteModel note = (NoteModel)objects[0];
        int noteId = note.getNoteId();
        String imageToDeletePath = note.getImageToDelete();

        ImageDAO imageDAO = new ImageDAO(mContext.getContentResolver());
        Cursor cursor = imageDAO.getNoteImages(noteId);
        String candidateThumbnailPath = null;

        while (cursor.moveToNext()) {
            String currentPath = cursor.getString(
                    cursor.getColumnIndex(DbContract.ImageEntry.COLUMN_IMAGE_PATH));
            boolean isThumbnail = cursor.getInt(
                    cursor.getColumnIndex(
                            DbContract.ImageEntry.COLUMN_THUMBNAIL)) == 1 ? true : false;
            if (currentPath.equals(imageToDeletePath)) {
                if (isThumbnail) {
                    continue;
                } else {
                    candidateThumbnailPath = null;
                    break;
                }
            } else {
                if (isThumbnail) {
                    candidateThumbnailPath = null;
                    break;
                } else {
                    if (candidateThumbnailPath == null) {
                        candidateThumbnailPath = currentPath;
                    } else {
                        continue;
                    }
                }
            }
        }

        // delete image from table
        imageDAO.deleteBy(imageToDeletePath);

        if (candidateThumbnailPath != null){
            imageDAO.updateToThumbnail(candidateThumbnailPath);
        }

        // delete image from external storage
        File image = new File(imageToDeletePath);
        if(image.exists()){
            image.delete();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(mListener != null){
            mListener.onDataDeleted();
        }
    }
}
