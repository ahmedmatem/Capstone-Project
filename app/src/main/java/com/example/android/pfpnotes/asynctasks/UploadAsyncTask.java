package com.example.android.pfpnotes.asynctasks;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.pfpnotes.common.Performance;
import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.data.Preferences;
import com.example.android.pfpnotes.models.Image;
import com.example.android.pfpnotes.models.Note;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ahmed on 25/03/2018.
 */

public class UploadAsyncTask extends AsyncTask<Context, Void, Void> {
    private FirebaseStorage mFirebaseStorage;
    private FirebaseFirestore mFirebaseFirestore;
    private Map<Note, List<Image>> mData;
    private String mUserName;
    private Context mContext;
    private OnUploadListener mListener;

    public interface OnUploadListener {
        void onNotesProgressChanged(int currentNoteNumber);

        void onImagesProgressChanged(int currentImageNumber);
    }

    private int mNumberOfUploadedNotes = 0;
    private int mNumberOfUploadedImages = 0;

    public UploadAsyncTask(OnUploadListener listener, Map<Note, List<Image>> data) {
        mListener = listener;
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mData = data;
    }

    private int getNumberOfImages(Map<Note, List<Image>> data) {
        int numberOfImages = 0;
        if (data == null) return numberOfImages;
        for (Map.Entry<Note, List<Image>> entry : data.entrySet()) {
            if (entry.getValue() != null) {
                numberOfImages += entry.getValue().size();
            }
        }
        return numberOfImages;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        mContext = contexts[0];
        // path: images/username/{image_name}
        mUserName = new Preferences(mContext).readUserEmail().split("@")[0];
        String imagePath = "images/" + mUserName + "/";
        for (Map.Entry<Note, List<Image>> entry : mData.entrySet()) {
            upload(entry, imagePath);
        }
        return null;
    }

    private void upload(Map.Entry<Note, List<Image>> entry, String imagePath) {
        StorageReference userImageReference;
        List<Image> images = entry.getValue();
        if (images != null) {
            String fileName = null;
            Uri localImageUri = null;
            File image;
            for (Image img : images) {
                localImageUri = Uri.fromFile(new File(img.getPath()));
                fileName = localImageUri.getLastPathSegment();
                userImageReference = mFirebaseStorage.getReference(imagePath + fileName);
                uploadImage(img.getPath(), userImageReference, entry);
            }
        }
    }

    private void uploadImage(
            String path,
            StorageReference imageRef,
            final Map.Entry<Note, List<Image>> entry) {
        Bitmap bitmap = Performance.decodeSampledBitmapFromFile(
                path,
                Performance.IN_SAMPLE_SIZE_REQ_WIDTH,
                Performance.IN_SAMPLE_SIZE_REQ_HEIGHT);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, Performance.COMPRESS_QUALITY, outputStream);
        byte[] data = outputStream.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (mListener != null) {
                    mListener.onImagesProgressChanged(++mNumberOfUploadedImages);
                }
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                uploadNote(entry, downloadUrl);
            }
        });
    }

    private void uploadNote(final Map.Entry<Note, List<Image>> entry, Uri downloadUrl) {
        Note note = entry.getKey();
        Map<String, Object> remoteNote = new HashMap<>();
        remoteNote.put("id", note.getId());
        remoteNote.put("email", note.getEmail());
        remoteNote.put("place", note.getPlace());
        remoteNote.put("width", note.getWidth());
        remoteNote.put("height", note.getHeight());
        remoteNote.put("layers", note.getLayers());
        remoteNote.put("copies", note.getCopies());
        remoteNote.put("status", note.getStatus());
        remoteNote.put("price", note.getPrice());
        remoteNote.put("date", note.getDate());
        if (downloadUrl != null) {
            remoteNote.put("downloadUrl", downloadUrl.toString());
        }

        // notes/{user-name}/remoteNote
        mFirebaseFirestore.collection("notes")
                .add(remoteNote)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String documentId = documentReference.getId();
                        updateLocalNote(entry, documentId);
                        if (mListener != null) {
                            mListener.onNotesProgressChanged(++mNumberOfUploadedNotes);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void updateLocalNote(Map.Entry<Note, List<Image>> entry, String documentId) {
        Note note = entry.getKey();
        ContentValues cv = new ContentValues();
        cv.put(DbContract.NoteEntry.COLUMN_STATUS, DbContract.NoteEntry.Status.DONE);
        cv.put(DbContract.NoteEntry.COLUMN_DOCUMENT_ID, documentId);
        mContext.getContentResolver().update(
                DbContract.NoteEntry.buildContentUriWithId(note.getId()),
                cv,
                null,
                null);
    }
}
