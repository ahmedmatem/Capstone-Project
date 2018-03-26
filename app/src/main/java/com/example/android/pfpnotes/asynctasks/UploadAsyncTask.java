package com.example.android.pfpnotes.asynctasks;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

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

import java.io.File;
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
    Context mContext;

    public UploadAsyncTask(Map<Note, List<Image>> data) {
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mData = data;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        mContext = contexts[0];
        // path: images/username/{image_name}
        mUserName = new Preferences(mContext).readUserEmail().split("@")[0];
        String imagePath = "images/" + mUserName + "/";
        for (Map.Entry<Note, List<Image>> entry : mData.entrySet()) {
            uploadImage(entry, imagePath);
        }
        return null;
    }

    //TODO: implement progress bar

    private void uploadImage(Map.Entry<Note, List<Image>> entry, String imagePath) {
        StorageReference userImageReference;
        List<Image> images = null;
        String fileName = null;
        Uri localImageUri = null;
        images = entry.getValue();
        if (images != null) {
            for (Image img : images) {
                localImageUri = Uri.fromFile(new File(img.getPath()));
                fileName = localImageUri.getLastPathSegment();
                userImageReference = mFirebaseStorage.getReference(imagePath + fileName);
                upload(entry, userImageReference, localImageUri);
            }
        } else {
            // note has no images
            uploadNote(entry, null);
        }
    }

    private void upload(final Map.Entry<Note, List<Image>> entry, StorageReference imageRef, Uri localImageUri) {
        UploadTask uploadTask = imageRef.putFile(localImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
        if(downloadUrl != null) {
            remoteNote.put("downloadUrl", downloadUrl.toString());
        }

        // notes/{user-name}/remoteNote
        mFirebaseFirestore.collection("notes")
                .add(remoteNote)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        updateLocalNote(entry);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void updateLocalNote(Map.Entry<Note, List<Image>> entry) {
        Note note = entry.getKey();
        ContentValues cv = new ContentValues();
        cv.put(DbContract.NoteEntry.COLUMN_STATUS, DbContract.NoteEntry.NoteStatus.STATUS_UPDATE);
        mContext.getContentResolver().update(
                DbContract.NoteEntry.buildContentUriWithId(note.getId()),
                cv,
                null,
                null);
    }
}
