package com.lym.trample;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.lym.trample.bean.UpdateBean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class App {

    private final static long APP_SERIAL_NUMBER = 1;

    public static boolean sCanUpdate = false;

    public static UpdateBean sUpdateItem;

    public static void checkUpdate(Context context) {
        BmobQuery<UpdateBean> query = new BmobQuery<UpdateBean>();
        query.findObjects(context, new FindListener<UpdateBean>() {

            @Override
            public void onSuccess(List<UpdateBean> list) {
                if(list != null && list.size() > 0)  {
                    UpdateBean item = getNewestVersionItem(list);
                    if(item != null) {
                        sUpdateItem = item;
                        sCanUpdate = true;
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private static UpdateBean getNewestVersionItem(List<UpdateBean> list) {
        int i = -1;
        int index = i;
        long maxSerialNumber = APP_SERIAL_NUMBER;
        for(UpdateBean item : list) {
            i++;
            if(item.getSerialNumber() > maxSerialNumber) {
                maxSerialNumber = item.getSerialNumber();
                index = i;
            }
        }
        if(index >= 0 && index < list.size()) {
            return list.get(index);
        } else {
            return null;
        }
    }
}
