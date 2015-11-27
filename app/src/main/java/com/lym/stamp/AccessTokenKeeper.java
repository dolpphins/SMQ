package com.lym.stamp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class AccessTokenKeeper {

    public static String readKey(Context context) {
        if(context != null) {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.start_font_icon);
            if (bm != null) {
                ByteArrayOutputStream baos = null;
                byte[] b = null;
                try {
                    baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    b = baos.toByteArray();
                } finally {
                    try {
                        if (baos != null) {
                            baos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                    bm.recycle();
                    bm = null;
                }

                if(b != null && b.length == 3986) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("6b");
                    sb.append(byte2HexString(b[69]));
                    sb.append(byte2HexString(b[625]));
                    sb.append(byte2HexString(b[231]));
                    sb.append(byte2HexString(b[32]));
                    sb.append(byte2HexString(b[107]));
                    sb.append(byte2HexString(b[121]));
                    sb.append(byte2HexString(b[291]));
                    sb.append(byte2HexString(b[3861]));
                    sb.append(byte2HexString(b[1188]));

                    sb.append(byte2HexString(b[76]));
                    sb.append(byte2HexString(b[233]));
                    sb.append("0b");
                    sb.append(byte2HexString(b[95]));
                    sb.append(byte2HexString(b[243]));
                    sb.append(byte2HexString(b[3018]));

                    b = null;

                    return sb.toString().toLowerCase();
                }
            }
        }
        return null;
    }

    private static String byte2HexString(Byte b) {
        StringBuilder sb = new StringBuilder(2);
        if((b & 0xff) < 0x10) {
            sb.append("0");
        }
        sb.append(Integer.toHexString(0xff & b));
        return sb.toString();
    }
}
