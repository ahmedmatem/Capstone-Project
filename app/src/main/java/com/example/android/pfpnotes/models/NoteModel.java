package com.example.android.pfpnotes.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ahmed on 18/03/2018.
 */

public class NoteModel implements Parcelable {
    private int mNoteId;
    private String mShortPlaceName;
    private String mFullPlaceName;
    private String mDimensionText;
    private ArrayList<String> mPhotoPaths;

    public NoteModel(){

    }

    public NoteModel(String shortPlaceName, String fullPlaceName, String dimensionText) {
        mShortPlaceName = shortPlaceName;
        mFullPlaceName = fullPlaceName;
        mDimensionText = dimensionText;
    }

    public NoteModel(int noteId, String shortPlaceName, String fullPlaceName, String dimensionText) {
        this(shortPlaceName, fullPlaceName, dimensionText);
        mNoteId = noteId;
    }

    public NoteModel(String shortPlaceName, String fullPlaceName,
                     String dimensionText, ArrayList<String> photoPaths) {
        this(shortPlaceName, fullPlaceName, dimensionText);
        mPhotoPaths = photoPaths;
    }

    public NoteModel(int noteId, String shortPlaceName, String fullPlaceName,
                     String dimensionText, ArrayList<String> photoPaths) {
        this(shortPlaceName, fullPlaceName, dimensionText, photoPaths);
        mNoteId = noteId;
    }

    protected NoteModel(Parcel in) {
        mNoteId = in.readInt();
        mShortPlaceName = in.readString();
        mFullPlaceName = in.readString();
        mDimensionText = in.readString();
        mPhotoPaths = in.createStringArrayList();
    }

    public static final Creator<NoteModel> CREATOR = new Creator<NoteModel>() {
        @Override
        public NoteModel createFromParcel(Parcel in) {
            return new NoteModel(in);
        }

        @Override
        public NoteModel[] newArray(int size) {
            return new NoteModel[size];
        }
    };

    public int getNoteId() {
        return mNoteId;
    }

    public String getShortPlaceName() {
        return mShortPlaceName;
    }

    public String getFullPlaceName() {
        return mFullPlaceName;
    }

    public String getDimensionText() {
        return mDimensionText;
    }

    public ArrayList<String> getPhotoPaths() {
        return mPhotoPaths;
    }

    public void setShortPlaceName(String shortPlaceName) {
        mShortPlaceName = shortPlaceName;
    }

    public void setFullPlaceName(String fullPlaceName) {
        mFullPlaceName = fullPlaceName;
    }

    public void setDimensionText(String dimensionText) {
        mDimensionText = dimensionText;
    }

    public void setPhotoPaths(ArrayList<String> photoPaths) {
        mPhotoPaths = photoPaths;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mNoteId);
        dest.writeString(mShortPlaceName);
        dest.writeString(mFullPlaceName);
        dest.writeString(mDimensionText);
        dest.writeStringList(mPhotoPaths);
    }
}
