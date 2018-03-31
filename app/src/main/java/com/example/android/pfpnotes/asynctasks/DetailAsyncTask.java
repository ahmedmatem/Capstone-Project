package com.example.android.pfpnotes.asynctasks;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.models.Image;
import com.example.android.pfpnotes.models.Note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailAsyncTask extends AsyncTask<Context, Void, Map<Note, List<Image>>> {
    private DetailListener mListener;
    public interface DetailListener{
        void onDetailLoadFinished(Map<Note, List<Image>> data);
    }

    public DetailAsyncTask(DetailListener listener) {
        mListener = listener;
    }

    @Override
    protected Map<Note, List<Image>> doInBackground(Context... contexts) {
        Context context = contexts[0];
        ContentResolver contentResolver = context.getContentResolver();
        List<Note> notes = getNotesSortedByDate(contentResolver);
        return collectImagesForEachNote(contentResolver, notes);
    }

    private List<Note> getNotesSortedByDate(ContentResolver contentResolver) {
        List<Note> noteList = null;
        Cursor cursor = contentResolver.query(
                DbContract.NoteEntry.CONTENT_URI,
                null,
                null,
                null,
                DbContract.NoteEntry.COLUMN_DATE + " DESC");
        if (cursor != null) {
            Note note;
            int position = 0;
            noteList = new ArrayList<>();
            while (cursor.moveToNext()) {
                note = new Note(cursor);
                note.setPosition(position++);
                noteList.add(note);
            }
            cursor.close();
        }
        return noteList;
    }

    private Map<Note, List<Image>> collectImagesForEachNote(
            ContentResolver contentResolver,
            List<Note> notes) {
        Cursor cursor = contentResolver.query(
                DbContract.ImageEntry.CONTENT_URI,
                null,
                null,
                null,
                DbContract.ImageEntry.COLUMN_NOTE_ID + " DESC");
        // map noteId --> List<Image>
        @SuppressLint("UseSparseArrays")
        Map<Integer, List<Image>> noteImages = new HashMap<>();
        List<Image> images = null;
        int noteId = -1;
        Image currentImage;
        if(cursor != null) {
            while (cursor.moveToNext()) {
                int currentNoteId = cursor.getInt(
                        cursor.getColumnIndex(DbContract.ImageEntry.COLUMN_NOTE_ID));
                currentImage = new Image(cursor);
                if (currentNoteId == noteId) {
                    if(images != null) {
                        images.add(currentImage);
                    }
                } else {
                    noteImages.put(noteId, images); // create one unnecessary entry with id = -1
                    // and null image list
                    noteId = currentNoteId;
                    images = new ArrayList<>();
                    images.add(currentImage);
                }
            }
            // put last images for last noteId
            noteImages.put(noteId, images);

            cursor.close();
        }

        Map<Note, List<Image>> data = new HashMap<>();
        List<Image> imageList;
        for(Note note : notes){
            imageList = noteImages.get(note.getId());
            data.put(note, imageList);
        }

        return data;
    }

    @Override
    protected void onPostExecute(Map<Note, List<Image>> data) {
        if(mListener != null){
            mListener.onDetailLoadFinished(data);
        }
    }
}
