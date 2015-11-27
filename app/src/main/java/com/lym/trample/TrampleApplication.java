package com.lym.trample;

import android.app.Application;
import android.util.Log;

import com.lym.trample.bean.TUser;
import com.lym.trample.bean.UpdateBean;

import java.util.UUID;

import cn.bmob.v3.Bmob;

public class TrampleApplication extends Application {

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
