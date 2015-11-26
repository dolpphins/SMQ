package com.lym.trample.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.client.params.ClientPNames;

/**
 * Created by mao on 2015/11/24.
 *
 * 网络工具类
 */
public class NetworkHelper {

    /**
     * 判断网络是否可用
     *
     * @param context 不可为null
     * @return 网络可用返回true,不可用返回false.注意context为null也会返回false.
     */
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
