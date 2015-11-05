package com.lym.trample;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by mao on 2015/11/5.
 */
public class TrampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Bmob配置
        Bmob.initialize(getApplicationContext(), AccessTokenKeeper.APPLICATION_ID);
    }
}
