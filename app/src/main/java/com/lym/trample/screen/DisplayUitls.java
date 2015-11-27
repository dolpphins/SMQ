package com.lym.trample.screen;

import android.content.Context;
import android.app.Activity;
import android.util.DisplayMetrics;

public class DisplayUitls {

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int sp2dp(Context context, float spValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float scale = metrics.scaledDensity / metrics.density;
        return (int) (spValue / scale + 0.5f);
    }

    public static int dp2sp(Context context, float dpValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float scale = metrics.scaledDensity / metrics.density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getScreenWidthPixels(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getScreenHeightPixels(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }
}
