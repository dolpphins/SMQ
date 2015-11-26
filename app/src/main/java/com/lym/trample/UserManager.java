package com.lym.trample;

import android.content.Context;

import com.lym.trample.bean.TUser;
import com.lym.trample.conf.SpConfig;
import com.lym.trample.utils.SharePreferencesManager;

import java.util.UUID;

/**
 * Created by mao on 2015/11/24.
 */
public class UserManager {

    private final static UserManager mUserManager = new UserManager();

    public static UserManager getInstance() {
        return mUserManager;
    }

    public void init(Context context) {
        if(context == null) {
            return;
        }
        SharePreferencesManager sp = SharePreferencesManager.getInstance();

//        //所有用户
//        ScoresManager.totalUserCount = sp.getInt(context, SpConfig.sTotalUserCount, 0);
        ScoresManager.bestColorScore = sp.getInt(context, SpConfig.sBestColorScore, 0);
        ScoresManager.bestDigitScore = sp.getInt(context, SpConfig.sBestDigitScore, 0);
        ScoresManager.bestLineScore = sp.getInt(context, SpConfig.sBestLineScore, 0);
//        //个人分数
        ScoresManager.bestUserColorScore = sp.getInt(context, SpConfig.sColorScore, 0);
        ScoresManager.bestUserDigitScore = sp.getInt(context, SpConfig.sDigitScore, 0);
        ScoresManager.bestUserLineScore = sp.getInt(context, SpConfig.sLineScore, 0);
        ScoresManager.guid = sp.getString(context, SpConfig.GUID, UUID.randomUUID().toString());

        //尝试保存
        sp.putString(context, SpConfig.GUID, ScoresManager.guid);
    }
}
