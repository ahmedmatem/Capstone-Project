package com.example.android.pfpnotes;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.android.pfpnotes.data.DbContract;

public class PriceListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "PriceListActivity";

    private static final int PRICE_LIST_LOADER = 0x01;

    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);

        String[] fromColumns = {DbContract.PriceEntry.COLUMN_INTERVAL,
                DbContract.PriceEntry.COLUMN_PRICE};
        int[] toViews = {R.id.interval, R.id.price};
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.price_list_item, null, fromColumns, toViews, 0);
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(columnIndex == cursor.getColumnIndex(DbContract.PriceEntry.COLUMN_PRICE)){
                    double price = cursor.getDouble(columnIndex);
                    TextView tv_price = (TextView) view;
                    tv_price.setText(String.format("£%.2f", price));
                    return true;
                }
                return false;
            }
        });

        ListView priceList = (ListView) findViewById(R.id.price_list);
        priceList.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(PRICE_LIST_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                DbContract.PriceEntry.CONTENT_URI,
                null, null, null,
                DbContract.PriceEntry.COLUMN_PRICE); // COLUMN_PRICE + " DESC"
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
