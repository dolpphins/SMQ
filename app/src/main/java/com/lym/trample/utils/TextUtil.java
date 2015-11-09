package com.lym.trample.utils;

import android.graphics.Rect;

/**
 * Created by mao on 2015/11/8.
 *
 * 文本相关工具类
 */
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

}
