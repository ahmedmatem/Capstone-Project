package com.example.android.pfpnotes;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.android.pfpnotes.data.DbContract;

public class PlaceListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "PlaceListActivity";
    public static final int PLACE_LIST_LOADER = 2;

    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        String[] fromColumns = {DbContract.PlaceEntry.COLUMN_NAME,
                DbContract.PlaceEntry.COLUMN_SHORT_NAME};
        int[] toViews = {R.id.full_name, R.id.short_name};
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.place_list_item, null, fromColumns, toViews, 0);

        ListView placeList = (ListView) findViewById(R.id.place_list);
        placeList.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(PLACE_LIST_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                DbContract.PlaceEntry.CONTENT_URI,
                null,
                null,
                null,
                DbContract.PlaceEntry.COLUMN_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: data - " + data);
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
