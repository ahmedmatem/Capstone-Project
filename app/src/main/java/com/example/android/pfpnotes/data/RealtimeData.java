package com.example.android.pfpnotes.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Created by ahmed on 28/02/2018.
 */

public class RealtimeData {
    private static final String TAG = "RealtimeData";
    private static final String REFERENCE_VERSION = "version";

    public static final String CHILD_PRICES = "prices";
    public static final String CHILD_PRICE_LIST = "list";
    private static final String CHILD_START_DATE = "startDate";
    private static final String CHILD_PLACES = "places";

//    private static final String SQUARE_DELIMITER = "-";
//    private static final String CHAR_TO_BE_REPLACED = ",";
//    private static final String NEW_CHAR = ".";

    private DatabaseReference mDatabaseRef;

    private DatabaseReference mVersionRef;

    private Context mContext;
    private Preferences mPreferences;


    public RealtimeData(Context context) {
        mContext = context;
        mPreferences = new Preferences(mContext);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
        mVersionRef = mDatabase.getReference(REFERENCE_VERSION);
    }

    /**
     * Read data from database and listen for data change
     * <p>
     * The onDataChange() method is triggered once
     * when the listener is attached and again
     * every time the data changes, including the children.
     */
    public void read() {
        mVersionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String localVersion = mPreferences.readVersion();
                String remoteVersion = dataSnapshot.getValue().toString();
                if (!localVersion.equals(remoteVersion)) {
                    // write new version in preferences
                    mPreferences.writeVersion(remoteVersion);

                    addValueEventListener();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void addValueEventListener() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Double> prices =
                        (HashMap<String, Double>) dataSnapshot
                                .child(CHILD_PRICES)
                                .child(CHILD_PRICE_LIST).getValue();
                String startDate = dataSnapshot
                        .child(CHILD_PRICES)
                        .child(CHILD_START_DATE).getValue().toString();

                HashMap<String, String> places =
                        (HashMap<String, String>) dataSnapshot.child(CHILD_PLACES)
                                .getValue();

                // save data on the phone
                savePrices(startDate, prices);
                savePlaces(places);

                mDatabaseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void savePrices(String startDate, HashMap<String, Double> prices) {
        deletePrices();
        if (mContext != null) {
            ContentResolver contentResolver = mContext.getContentResolver();
            for (Entry<String, Double> entry : prices.entrySet()) {
                ContentValues cv = new ContentValues();
                cv.put(DbContract.PriceEntry.COLUMN_INTERVAL, entry.getKey());
                cv.put(DbContract.PriceEntry.COLUMN_PRICE, entry.getValue());
                cv.put(DbContract.PriceEntry.COLUMN_START_DATE, startDate);
                contentResolver.insert(DbContract.PriceEntry.CONTENT_URI, cv);
            }
        }
    }

    private void savePlaces(HashMap<String, String> places) {
        deletePlaces();
        if (mContext != null) {
            ContentResolver contentResolver = mContext.getContentResolver();
            for (Entry<String, String> entry : places.entrySet()) {
                ContentValues cv = new ContentValues();
                cv.put(DbContract.PlaceEntry.COLUMN_NAME, entry.getKey());
                cv.put(DbContract.PlaceEntry.COLUMN_SHORT_NAME, entry.getValue());
                contentResolver.insert(DbContract.PlaceEntry.CONTENT_URI, cv);
            }
        }
    }

    private void deletePrices() {
        if (mContext != null) {
            mContext.getContentResolver()
                    .delete(DbContract.PriceEntry.CONTENT_URI,
                            null,
                            null);
        }
    }

    private void deletePlaces() {
        if (mContext != null) {
            mContext.getContentResolver()
                    .delete(DbContract.PlaceEntry.CONTENT_URI,
                            null,
                            null);
        }
    }

//    public double getPriceBySquare(double square) {
//        Interval interval = null;
//        for (Entry<String, Double> entry : mPricePerSquare.entrySet()) {
//            interval = entryToInterval(entry);
//            if (interval.contain(square)) {
//                return interval.getValue();
//            }
//        }
//        return 0.0;
//    }
//
//    private Interval entryToInterval(Entry<String, Double> entry) {
//        String entryKey = entry.getKey(); // exm. 0,011-0,050
//        String[] splitEntry = entryKey
//                .replace(CHAR_TO_BE_REPLACED, NEW_CHAR) // 0.011-0.050
//                .split(SQUARE_DELIMITER);
//        double minValue = Double.valueOf(splitEntry[0]);
//        double maxValue = Double.valueOf(splitEntry[1]);
//        double value = entry.getValue();
//
//        return new Interval(minValue, maxValue, value);
//    }
}
