package com.example.android.pfpnotes.data.daos;

import android.content.ContentResolver;
import android.net.Uri;

import com.example.android.pfpnotes.models.NoteItem;

/**
 * Created by ahmed on 18/03/2018.
 */

public class NoteDAO {
    private ContentResolver mContentResolver;

    public NoteDAO(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public Uri insert(NoteItem noteItem){
        return null;
    }
}
