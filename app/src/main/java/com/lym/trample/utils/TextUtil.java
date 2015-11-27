package com.lym.trample.utils;

import android.graphics.Rect;
import android.text.TextUtils;

public class TextUtil {


    public static Rect getFillRectForText(Rect rect) {
        if(rect == null) {
            return null;
        }
        Rect r = new Rect();
        int textSize = 2 * rect.width() / 3;
        int medianLineX = (rect.left + rect.right) / 2;
        int medianLineY = (rect.top + rect.bottom) / 2;
        int medianTextSize = textSize / 2;
        r.left = medianLineX - medianTextSize;
        r.right = medianLineX + medianTextSize;
        r.top = medianLineY - medianTextSize;
        r.bottom = medianLineY + medianTextSize;

        return r;
    }

    public static String[] string2StringArray(String s) {
        if(TextUtils.isEmpty(s)) {
            return null;
        }
        String[] arr = new String[s.length()];
        char[] charArr = s.toCharArray();
        for(int i = 0; i < s.length(); i++) {
            arr[i] = charArr[i] + "";
        }
        return arr;
    }
}
