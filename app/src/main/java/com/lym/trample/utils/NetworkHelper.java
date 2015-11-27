package com.lym.trample.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.client.params.ClientPNames;

public class NetworkHelper {

    public static boolean isAvailable(Context context) {
        if(context == null) {
            return false;
        }
        ConnectivityManager cm =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if(ni != null) {
                return ni.isAvailable();
            }
        }
        return false;
    }
}
