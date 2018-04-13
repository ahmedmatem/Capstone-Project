package com.example.android.pfpnotes.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by ahmed on 01/03/2018.
 */

public class DataProvider extends ContentProvider {

    private DbHelper mDbHelper;

    public static final int PRICES = 100;
    public static final int PRICE_WITH_ID = 101;

    public static final int PLACES = 200;
    public static final int PLACE_WITH_ID = 201;

    public static final int NOTES = 300;
    public static final int NOTE_WITH_ID = 301;

    public static final int IMAGES = 400;
    public static final int IMAGE_WITH_ID = 401;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DbContract.AUTHORITY;
        // directories
        uriMatcher.addURI(authority, DbContract.PATH_PRICES, PRICES);
        uriMatcher.addURI(authority, DbContract.PATH_PLACES, PLACES);
        uriMatcher.addURI(authority, DbContract.PATH_NOTES, NOTES);
        uriMatcher.addURI(authority, DbContract.PATH_IMAGES, IMAGES);

        // individuals
        uriMatcher.addURI(authority, DbContract.PATH_PRICES + "/#", PRICE_WITH_ID);
        uriMatcher.addURI(authority, DbContract.PATH_PLACES + "/#", PLACE_WITH_ID);
        uriMatcher.addURI(authority, DbContract.PATH_NOTES + "/#", NOTE_WITH_ID);
        uriMatcher.addURI(authority, DbContract.PATH_IMAGES + "/#", IMAGE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        final int match = sUriMatcher.match(uri);

        String id;
        switch (match) {
            case PRICES:
                queryBuilder.setTables(DbContract.PriceEntry.TABLE_NAME);
                break;
            case PLACES:
                queryBuilder.setTables(DbContract.PlaceEntry.TABLE_NAME);
                break;
            case NOTES:
                queryBuilder.setTables(DbContract.NoteEntry.TABLE_NAME +
                " LEFT OUTER JOIN " + DbContract.ImageEntry.TABLE_NAME + " ON " +
                        DbContract.NoteEntry.FULL_ID + "=" + DbContract.ImageEntry.COLUMN_NOTE_ID +
                " AND " + DbContract.ImageEntry.COLUMN_THUMBNAIL + "=1");
                queryBuilder.setProjectionMap(DbContract.NoteEntry.sNoteProjectionMap);
                break;
            case IMAGES:
                queryBuilder.setTables(DbContract.ImageEntry.TABLE_NAME);
                break;
            case PRICE_WITH_ID:
                queryBuilder.setTables(DbContract.PriceEntry.TABLE_NAME);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DbContract.PriceEntry._ID + "=" + id);
                break;
            case PLACE_WITH_ID:
                queryBuilder.setTables(DbContract.PlaceEntry.TABLE_NAME);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DbContract.PlaceEntry._ID + "=" + id);
                break;
            case NOTE_WITH_ID:
                queryBuilder.setTables(DbContract.NoteEntry.TABLE_NAME);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DbContract.NoteEntry._ID + "=" + id);
                break;
            case IMAGE_WITH_ID:
                queryBuilder.setTables(DbContract.ImageEntry.TABLE_NAME);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DbContract.ImageEntry._ID + "=" + id
                );
                break;
            default:
                throw new UnsupportedOperationException("unknown uri: " + uri);
        }

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();


        Cursor returnCursor = queryBuilder.query(db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        // set a notification Uri on the Cursor
        if (returnCursor != null) {
            returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;
        switch (match) {
            case PRICES:
                id = db.insert(DbContract.PriceEntry.TABLE_NAME,
                        null,
                        values);
                if (id > 0) {
                    // success
                    returnUri = ContentUris.withAppendedId(
                            DbContract.PriceEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;

            case PLACES:
                id = db.insert(DbContract.PlaceEntry.TABLE_NAME,
                        null,
                        values);
                if (id > 0) {
                    // success
                    returnUri = ContentUris.withAppendedId(
                            DbContract.PlaceEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case NOTES:
                id = db.insert(DbContract.NoteEntry.TABLE_NAME,
                        null,
                        values);
                if (id > 0) {
                    // success
                    returnUri = ContentUris.withAppendedId(
                            DbContract.NoteEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case IMAGES:
                id = db.insert(DbContract.ImageEntry.TABLE_NAME,
                        null,
                        values);
                if (id > 0) {
                    // success
                    returnUri = ContentUris.withAppendedId(
                            DbContract.ImageEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int numberOfDeletedRows;
        String id;
        String mSelection;
        String[] mSelectionArgs;
        switch (match) {
            case PRICE_WITH_ID:
                id = uri.getPathSegments().get(1);
                mSelection = DbContract.PriceEntry._ID + "=?";
                mSelectionArgs = new String[]{id};
                numberOfDeletedRows = db.delete(DbContract.PriceEntry.TABLE_NAME,
                        mSelection,
                        mSelectionArgs);
                break;
            case PLACE_WITH_ID:
                id = uri.getPathSegments().get(1);
                mSelection = DbContract.PlaceEntry._ID + "=?";
                mSelectionArgs = new String[]{id};
                numberOfDeletedRows = db.delete(DbContract.PlaceEntry.TABLE_NAME,
                        mSelection,
                        mSelectionArgs);
                break;
            case NOTE_WITH_ID:
                id = uri.getPathSegments().get(1);
                mSelection = DbContract.NoteEntry._ID + "=?";
                mSelectionArgs = new String[]{id};
                numberOfDeletedRows = db.delete(DbContract.NoteEntry.TABLE_NAME,
                        mSelection,
                        mSelectionArgs);
                break;
            case IMAGE_WITH_ID:
                id = uri.getPathSegments().get(1);
                mSelection = DbContract.ImageEntry._ID + "=?";
                mSelectionArgs = new String[]{id};
                numberOfDeletedRows = db.delete(DbContract.ImageEntry.TABLE_NAME,
                        mSelection,
                        mSelectionArgs);
                break;
            case PRICES:
                numberOfDeletedRows = db.delete(DbContract.PriceEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case PLACES:
                numberOfDeletedRows = db.delete(DbContract.PlaceEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case NOTES:
                numberOfDeletedRows = db.delete(DbContract.NoteEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case IMAGES:
                numberOfDeletedRows = db.delete(DbContract.ImageEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return numberOfDeletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int numberOfUpdatedRows;
        String id;
        String mSelection;
        String[] mSelectionArgs;
        switch (match) {
            case PRICES:
                numberOfUpdatedRows =
                        db.update(DbContract.PriceEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PLACES:
                numberOfUpdatedRows =
                        db.update(DbContract.PlaceEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOTES:
                numberOfUpdatedRows =
                        db.update(DbContract.NoteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case IMAGES:
                numberOfUpdatedRows =
                        db.update(DbContract.ImageEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PRICE_WITH_ID:
                id = uri.getPathSegments().get(1);
                mSelection = DbContract.PriceEntry._ID + "=?";
                mSelectionArgs = new String[]{id};
                numberOfUpdatedRows = db.update(DbContract.PriceEntry.TABLE_NAME,
                        values,
                        mSelection,
                        mSelectionArgs);
                break;
            case PLACE_WITH_ID:
                id = uri.getPathSegments().get(1);
                mSelection = DbContract.PlaceEntry._ID + "=?";
                mSelectionArgs = new String[]{id};
                numberOfUpdatedRows = db.update(DbContract.PlaceEntry.TABLE_NAME,
                        values,
                        mSelection,
                        mSelectionArgs);
                break;
            case NOTE_WITH_ID:
                id = uri.getPathSegments().get(1);
                mSelection = DbContract.NoteEntry._ID + "=?";
                mSelectionArgs = new String[]{id};
                numberOfUpdatedRows = db.update(DbContract.NoteEntry.TABLE_NAME,
                        values,
                        mSelection,
                        mSelectionArgs);
                break;
            case IMAGE_WITH_ID:
                id = uri.getPathSegments().get(1);
                mSelection = DbContract.ImageEntry._ID + "=?";
                mSelectionArgs = new String[]{id};
                numberOfUpdatedRows = db.update(DbContract.ImageEntry.TABLE_NAME,
                        values,
                        mSelection,
                        mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return numberOfUpdatedRows;
    }
}
