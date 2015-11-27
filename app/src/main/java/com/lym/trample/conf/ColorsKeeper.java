package com.lym.trample.conf;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorsKeeper {

    private final static Map<String, String> colorsMap = new HashMap<String, String>();

    static {
        colorsMap.put("黑", "#000000");
        colorsMap.put("红", "#ff0000");
        colorsMap.put("蓝", "#0000ff");
        colorsMap.put("橙", "#ff7d00");
        colorsMap.put("棕", "#802a2a");
        colorsMap.put("紫", "#8b00ff");
    }

    public static Map<String, String> getColorsMap() {
        return colorsMap;
    }
}




