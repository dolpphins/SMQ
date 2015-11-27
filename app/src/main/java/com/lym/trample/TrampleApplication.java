package com.lym.trample;

import android.app.Application;
import android.util.Log;

import com.lym.trample.bean.TUser;
import com.lym.trample.bean.UpdateBean;

import java.util.UUID;

import cn.bmob.v3.Bmob;

/**
 * Created by mao on 2015/11/5.
 */
public class TrampleApplication extends Application {

    private final static String TAG = "TrampleApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Bmob配置
        Bmob.initialize(getApplicationContext(), AccessTokenKeeper.readKey(getApplicationContext()));

        //初始化本地数据
        UserManager.getInstance().init(getApplicationContext());

        NetworkDataManager ndm = new NetworkDataManager(getApplicationContext());
        //如果是新用户则创建
        TUser user = new TUser();
        user.setGuid(ScoresManager.guid);
        ndm.insertOneUserToBmob(user);

        //获取数据
        ndm.requestUpdateData();

        //检查更新
        App.checkUpdate(getApplicationContext());

    }
}
