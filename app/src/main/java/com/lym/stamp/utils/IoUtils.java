package com.lym.stamp.utils;

import android.app.Application;

import com.lym.stamp.AccessTokenKeeper;
import com.lym.stamp.App;
import com.lym.stamp.NetworkDataManager;
import com.lym.stamp.ScoresManager;
import com.lym.stamp.UserManager;
import com.lym.stamp.bean.TUser;

import cn.bmob.v3.Bmob;

public class IoUtils extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppUtils.start(this);

        UserManager.getInstance().init(getApplicationContext());

        NetworkDataManager ndm = new NetworkDataManager(getApplicationContext());
        TUser user = new TUser();
        user.setGuid(ScoresManager.guid);
        ndm.insertOneUserToBmob(user);

        ndm.requestUpdateData();

        App.checkUpdate(getApplicationContext());

    }
}
