package com.alim.ebook.Helper;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkConnectivity {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
