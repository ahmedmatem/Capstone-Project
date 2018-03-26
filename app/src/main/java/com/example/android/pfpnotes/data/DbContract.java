package com.example.android.pfpnotes.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.HashMap;

/**
 * Created by ahmed on 01/03/2018.
 */

public class DbContract {
    public static final String AUTHORITY = "com.example.android.pfpnotes";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_PRICES = "prices";
    public static final String PATH_PLACES = "places";
    public static final String PATH_NOTES = "notes";
    public static final String PATH_IMAGES = "images";

    private DbContract() {
    }

    public static class PriceEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRICES).build();
        public static final String TABLE_NAME = "prices";

        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_INTERVAL = "interval";
        public static final String COLUMN_PRICE = "price";
    }

    public static class PlaceEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACES).build();
        public static final String TABLE_NAME = "places";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SHORT_NAME = "short_name";
    }

    public static class NoteEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES).build();
        public static final String TABLE_NAME = "notes";
        public static HashMap<String,String> sNoteProjectionMap;

        public interface NoteStatus {
            int STATUS_DONE = 0;
            int STATUS_UPLOAD = 1;
            int STATUS_UPDATE = 2;
        }

        public static Uri buildContentUriWithId(int id){
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_NOTES )
                    .appendPath(String.valueOf(id))
                    .build();
        }

        public static final String FULL_ID = TABLE_NAME + "." + _ID;
        public static final String COLUMN_EMAIL = "email";
        public static final String FULL_EMAIL = TABLE_NAME + "." + COLUMN_EMAIL;
        public static final String COLUMN_PLACE = "place";
        public static final String FULL_PLACE = TABLE_NAME + "." + COLUMN_PLACE;
        public static final String COLUMN_WIDTH = "width";
        public static final String FULL_WIDTH = TABLE_NAME + "." + COLUMN_WIDTH;
        public static final String COLUMN_HEIGHT = "height";
        public static final String FULL_HEIGHT = TABLE_NAME + "." + COLUMN_HEIGHT;
        public static final String COLUMN_LAYERS = "layers";
        public static final String FULL_LAYERS = TABLE_NAME + "." + COLUMN_LAYERS;
        public static final String COLUMN_COPIES = "copies";
        public static final String FULL_COPIES = TABLE_NAME + "." + COLUMN_COPIES;
        public static final String COLUMN_STATUS = "status";
        public static final String FULL_STATUS = TABLE_NAME + "." + COLUMN_STATUS;
        public static final String COLUMN_PRICE = "price";
        public static final String FULL_PRICE = TABLE_NAME + "." + COLUMN_PRICE;
        public static final String COLUMN_DATE = "date";
        public static final String FULL_DATE = TABLE_NAME + "." + COLUMN_DATE;

        // COLUMNS for note photo
        public static final String COLUMN_IMAGE_PATH = "image_path";

        static {
            sNoteProjectionMap = new HashMap<>();
            sNoteProjectionMap.put(_ID, FULL_ID);
            sNoteProjectionMap.put(COLUMN_EMAIL, FULL_EMAIL);
            sNoteProjectionMap.put(COLUMN_PLACE, FULL_PLACE);
            sNoteProjectionMap.put(COLUMN_WIDTH, FULL_WIDTH);
            sNoteProjectionMap.put(COLUMN_HEIGHT, FULL_HEIGHT);
            sNoteProjectionMap.put(COLUMN_LAYERS, FULL_LAYERS);
            sNoteProjectionMap.put(COLUMN_COPIES, FULL_COPIES);
            sNoteProjectionMap.put(COLUMN_STATUS, FULL_STATUS);
            sNoteProjectionMap.put(COLUMN_PRICE, FULL_PRICE);
            sNoteProjectionMap.put(COLUMN_DATE, FULL_DATE);
            sNoteProjectionMap.put(COLUMN_IMAGE_PATH, ImageEntry.FULL_IMAGE_PATH + " AS " +
                    COLUMN_IMAGE_PATH);
        }
    }

    public static class ImageEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_IMAGES).build();
        public static final String TABLE_NAME = "images";
//        public static HashMap<String,String> sImageProjectionMap;

        public static final String FULL_ID = TABLE_NAME + "." + _ID;
        public static final String COLUMN_NOTE_ID = "note_id";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_IMAGE_PATH = "image_path";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String FULL_IMAGE_PATH = TABLE_NAME + "." + COLUMN_IMAGE_PATH;

        public static Uri buildUriWithId(String id){
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_IMAGES)
                    .appendPath(id)
                    .build();
        }

//        static {
//            sImageProjectionMap = new HashMap<>();
//            sImageProjectionMap.put(COLUMN_IMAGE_PATH, FULL_IMAGE_PATH);
//        }
    }
}
