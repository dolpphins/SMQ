package com.lym.trample.conf;

import android.content.Context;
import android.content.res.Resources;

import com.lym.stamp.R;

import java.util.HashMap;
import java.util.Map;

public class ColorsKeeper {

    private static Map<String, Integer> colorsMap;

    public static Map<String, Integer> getColorsMap(Context context) {
        if(colorsMap == null && context != null) {
            colorsMap = new HashMap<String, Integer>();

            Resources res = context.getResources();
            colorsMap.put(res.getString(R.string.black_color_text), res.getColor(R.color.black));
            colorsMap.put(res.getString(R.string.red_color_text), res.getColor(R.color.red));
            colorsMap.put(res.getString(R.string.blue_color_text), res.getColor(R.color.blue));
            colorsMap.put(res.getString(R.string.orange_color_text), res.getColor(R.color.orange));
            colorsMap.put(res.getString(R.string.palm_color_text), res.getColor(R.color.palm));
            colorsMap.put(res.getString(R.string.purple_color_text), res.getColor(R.color.purple));
        }
        return colorsMap;

    }
}




