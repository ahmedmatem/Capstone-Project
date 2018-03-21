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
    private ArrayList<String> mPaths;
    private String mImageToDelete;

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
                     String dimensionText, ArrayList<String> paths) {
        this(shortPlaceName, fullPlaceName, dimensionText);
        mPaths = paths;
    }

    public NoteModel(int noteId, String shortPlaceName, String fullPlaceName,
                     String dimensionText, ArrayList<String> paths) {
        this(shortPlaceName, fullPlaceName, dimensionText, paths);
        mNoteId = noteId;
    }

    protected NoteModel(Parcel in) {
        mNoteId = in.readInt();
        mShortPlaceName = in.readString();
        mFullPlaceName = in.readString();
        mDimensionText = in.readString();
        mPaths = in.createStringArrayList();
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

    public ArrayList<String> getPaths() {
        return mPaths;
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

    public void setPaths(ArrayList<String> paths) {
        mPaths = paths;
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
        dest.writeStringList(mPaths);
    }

    public String getImageToDelete() {
        return mImageToDelete;
    }

    public void setImageToDelete(String imageToDelete) {
        mImageToDelete = imageToDelete;
    }
}
