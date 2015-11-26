package com.lym.trample.utils;

import android.graphics.Rect;
import android.text.TextUtils;

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

    /**
     * String转String数组，String每一个字符对应数组中一个元素
     *
     * @param s 要转换的String对象
     * @return 转换成功返回相应的String数组，转换失败返回null.
     */
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
