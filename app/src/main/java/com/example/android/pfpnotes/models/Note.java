package com.example.android.pfpnotes.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.pfpnotes.data.DateHelper;
import com.example.android.pfpnotes.data.DbContract;

/**
 * Created by ahmed on 24/03/2018.
 */

public class Note implements Parcelable {
    private static final String DELIMITER = "x";

    private int id;
    private String email;
    private String place;
    private int width;
    private int height;
    private int layers;
    private int copies;
    private int status;
    private double price;
    private String date;
    private int position; // position in set

    public Note(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(DbContract.NoteEntry._ID));
        email = cursor.getString(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_EMAIL));
        place = cursor.getString(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_PLACE));
        width = cursor.getInt(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_WIDTH));
        height = cursor.getInt(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_HEIGHT));
        layers = cursor.getInt(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_LAYERS));
        copies = cursor.getInt(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_COPIES));
        status = cursor.getInt(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_STATUS));
        price = cursor.getDouble(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_PRICE));
        date = cursor.getString(cursor.getColumnIndex(DbContract.NoteEntry.COLUMN_DATE));
    }

    public String getDimension() {
        StringBuilder dimension = new StringBuilder(width + DELIMITER + height);
        if (layers > 1) {
            dimension.append(DELIMITER).append(String.valueOf(layers));
        }
        if (copies > 1) {
            dimension.append(DELIMITER).append(String.valueOf(copies));
        }
        return dimension.toString();
    }

    protected Note(Parcel in) {
        id = in.readInt();
        email = in.readString();
        place = in.readString();
        width = in.readInt();
        height = in.readInt();
        layers = in.readInt();
        copies = in.readInt();
        status = in.readInt();
        price = in.readDouble();
        date = in.readString();
        position = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPlace() {
        return place;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLayers() {
        return layers;
    }

    public int getCopies() {
        return copies;
    }

    public int getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getSampleDate() {
        return DateHelper.getDate(date);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(place);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(layers);
        dest.writeInt(copies);
        dest.writeInt(status);
        dest.writeDouble(price);
        dest.writeString(date);
        dest.writeInt(position);
    }
}
