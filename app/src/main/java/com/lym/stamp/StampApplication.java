package com.lym.stamp;

import android.app.Application;

import com.lym.stamp.bean.TUser;

import cn.bmob.v3.Bmob;

public class StampApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Bmob.initialize(getApplicationContext(), AccessTokenKeeper.readKey(getApplicationContext()));

        UserManager.getInstance().init(getApplicationContext());

        NetworkDataManager ndm = new NetworkDataManager(getApplicationContext());
        TUser user = new TUser();
        user.setGuid(ScoresManager.guid);
        ndm.insertOneUserToBmob(user);

        ndm.requestUpdateData();

        App.checkUpdate(getApplicationContext());

    }
}
