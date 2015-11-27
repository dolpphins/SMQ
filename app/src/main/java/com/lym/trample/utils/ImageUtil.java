package com.lym.trample.utils;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class ImageUtil {

    public static Rect getFillRectForBitmap(Bitmap bm, Rect r) {
        int width = r.width();
        int height = r.height();
        if(bm == null || width <= 0 || height <= 0) {
            return null;
        }
        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();
        if(bmWidth <= 0 || bmHeight <= 0) {
            return null;
        }

        float factor = 1.0f;
        float realWidth = bmWidth;
        float realHeight = bmHeight;
        Rect rect = new Rect();
        if(bmWidth * height < bmHeight * width) {
            factor = (float) height / (float) bmHeight;
            realWidth = factor * bmWidth;
            rect.top = r.top;
            rect.bottom = r.bottom;
            int medianLine = (r.left + r.right) / 2;
            rect.left = (int)(medianLine - realWidth / 2);
            rect.right = (int)(medianLine + realWidth / 2);
        } else {
            factor = (float) width / (float) bmWidth;
            realHeight = factor * bmHeight;
            rect.left = r.left;
            rect.right = r.right;
            int medianLine = (r.top + r.bottom) / 2;
            rect.top = (int)(medianLine - realHeight / 2);
            rect.bottom = (int)(medianLine + realHeight / 2);
        }
        return rect;
    }
}
