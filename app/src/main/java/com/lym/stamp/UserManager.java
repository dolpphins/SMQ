package com.lym.stamp;

import android.content.Context;

import com.lym.stamp.conf.SpConfig;
import com.lym.stamp.utils.SharePreferencesManager;

import java.util.UUID;

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

        ScoresManager.bestColorScore = sp.getInt(context, SpConfig.sBestColorScore, 0);
        ScoresManager.bestDigitScore = sp.getInt(context, SpConfig.sBestDigitScore, 0);
        ScoresManager.bestLineScore = sp.getInt(context, SpConfig.sBestLineScore, 0);
        ScoresManager.bestUserColorScore = sp.getInt(context, SpConfig.sColorScore, 0);
        ScoresManager.bestUserDigitScore = sp.getInt(context, SpConfig.sDigitScore, 0);
        ScoresManager.bestUserLineScore = sp.getInt(context, SpConfig.sLineScore, 0);
        ScoresManager.guid = sp.getString(context, SpConfig.GUID, UUID.randomUUID().toString());

        sp.putString(context, SpConfig.GUID, ScoresManager.guid);
    }
}
