package com.example.android.pfpnotes.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ahmed on 03/03/2018.
 */

public class Connection {
    private static Context mContext;

    public Connection(Context context) {
        mContext = context;
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
