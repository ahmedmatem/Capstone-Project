package com.example.android.pfpnotes.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.pfpnotes.R;


/**
 * Created by ahmed on 02/03/2018.
 */

public class Preferences {
    public static final String REAL_TIME_DATA_PREF =
            "com.example.android.pfpnotes.REALTIME_DATA_PREF";
    public static final String USER_DATA_PREF = "com.example.android.pfpnotes.USER_DATA_PREF";

    private Context mContext;

    private SharedPreferences mRealTimeDataPreferences;
    private SharedPreferences mUserPreferences;

    private static final String defaultUserEmail = "xxx";

    public Preferences(Context context) {
        mContext = context;
        mRealTimeDataPreferences =
                mContext.getSharedPreferences(REAL_TIME_DATA_PREF, Context.MODE_PRIVATE);
        mUserPreferences = mContext.getSharedPreferences(USER_DATA_PREF, Context.MODE_PRIVATE);

    }

    public void writeVersion(String version){
        mRealTimeDataPreferences
                .edit()
                .putString(mContext.getString(R.string.version_key), version)
                .commit();
    }

    public String readVersion(){
        return mRealTimeDataPreferences.getString(
                mContext.getString(R.string.version_key), "0");
    }

    public void writeUserEmail(String email){
        mUserPreferences.edit()
                .putString(mContext.getString(R.string.email_address), email)
                .commit();
    }

    public String readUserEmail(){
        return mUserPreferences.getString(mContext.getString(R.string.email_address),
                defaultUserEmail);
    }
}
