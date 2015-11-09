package com.lym.trample.conf;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mao on 2015/11/8.
 *
 * 保存预定于的颜色
 *
 * @author 麦灿标
 */
public class ColorsKeeper {

    /** 颜色map */
    private final static Map<String, String> colorsMap = new HashMap<String, String>();

    static {
        colorsMap.put("黑", "#000000");
        colorsMap.put("红", "#ff0000");
        colorsMap.put("蓝", "#0000ff");
        colorsMap.put("橙", "#ff7d00");
        colorsMap.put("棕", "#802a2a");
        colorsMap.put("紫", "#8b00ff");
    }

    /**
     * 获取预定义颜色map
     *
     * @return 返回预定义颜色map
     * */
    public static Map<String, String> getColorsMap() {
        return colorsMap;
    }
}




