package com.example.android.pfpnotes.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

public class Detail implements Parcelable {
    private Map.Entry<Note,List<Image>> mData;

    public Detail() {
    }

    public Detail(Map.Entry<Note,List<Image>> data) {
        mData = data;
    }

    protected Detail(Parcel in) {
    }

    public static final Creator<Detail> CREATOR = new Creator<Detail>() {
        @Override
        public Detail createFromParcel(Parcel in) {
            return new Detail(in);
        }

        @Override
        public Detail[] newArray(int size) {
            return new Detail[size];
        }
    };

    public Map.Entry<Note, List<Image>> getData() {
        return mData;
    }

    public void setData(Map.Entry<Note, List<Image>> data) {
        mData = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
