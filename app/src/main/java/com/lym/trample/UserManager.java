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
//        ScoresManager.bestColorScore = sp.getInt(context, SpConfig.sBestColorScore, 0);
//        ScoresManager.bestDigitScore = sp.getInt(context, SpConfig.sBestDigitScore, 0);
//        ScoresManager.bestLineScore = sp.getInt(context, SpConfig.sBestLineScore, 0);
//        //个人分数
//        ScoresManager.bestUserColorScore = sp.getInt(context, SpConfig.sColorScore, 0);
//        ScoresManager.bestUserDigitScore = sp.getInt(context, SpConfig.sDigitScore, 0);
//        ScoresManager.bestUserDigitRank = sp.getInt(context, SpConfig.sLineScore, 0);
        ScoresManager.guid = sp.getString(context, SpConfig.GUID, UUID.randomUUID().toString());
//        //个人排名
//        ScoresManager.bestUserColorRank = sp.getInt(context, SpConfig.sColorRanking, 0);
//        ScoresManager.bestUserDigitRank = sp.getInt(context, SpConfig.sDigitRanking, 0);
//        ScoresManager.bestUserLineRank = sp.getInt(context, SpConfig.sLineRanking, 0);


//        //尝试保存
//        sp.putInt(context, SpConfig.sTotalUserCount, ScoresManager.totalUserCount);
//        sp.getInt(context, SpConfig.sBestColorScore, ScoresManager.bestColorScore);
//        sp.getInt(context, SpConfig.sBestDigitScore, ScoresManager.bestDigitScore);
//        sp.getInt(context, SpConfig.sBestLineScore, ScoresManager.bestLineScore);
//
//        sp.putInt(context, SpConfig.sColorScore, ScoresManager.bestUserColorScore);
//        sp.putInt(context, SpConfig.sDigitScore, ScoresManager.bestUserDigitScore);
//        sp.putInt(context, SpConfig.sLineScore, ScoresManager.bestUserDigitRank);
        sp.putString(context, SpConfig.GUID, ScoresManager.guid);
//
//        sp.putInt(context, SpConfig.sColorRanking, ScoresManager.bestUserColorRank);
//        sp.putInt(context, SpConfig.sDigitRanking, ScoresManager.bestUserDigitRank);
//        sp.putInt(context, SpConfig.sLineRanking, ScoresManager.bestUserLineRank);
    }
}
