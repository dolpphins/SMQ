package com.lym.stamp.utils;

import android.content.Context;

import com.lym.stamp.AccessTokenKeeper;

import cn.bmob.v3.Bmob;

public class AppUtils {

    public static void start(Context context) {
        prepare(context);
    }

    private static void prepare(Context context) {
        realStart(context);
    }

    private static void realStart(Context context) {
        calculate();
        Bmob.initialize(context.getApplicationContext(), AccessTokenKeeper.readKey(context.getApplicationContext()));
        finish();
    }

    private static void finish() {

    }

    private static int calculate() {
        try {
            int i = 0;
            return i;
        } catch (Exception e) {
            return 0;
        }
    }
}
