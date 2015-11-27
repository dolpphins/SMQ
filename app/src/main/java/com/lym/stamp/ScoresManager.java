package com.lym.stamp;

import android.content.Context;

import com.lym.stamp.bean.TUser;
import com.lym.stamp.conf.SpConfig;
import com.lym.stamp.utils.SharePreferencesManager;

public class ScoresManager {

    public static enum Status {
        NONE, GETTING, FAIL,SUCCESS
    }


    public static int totalUserCount;

    public static int bestColorScore;

    public static Status bestColorScoreSuccess = Status.NONE;

    public static int bestDigitScore;

    public static Status bestDigitScoreSuccess = Status.NONE;

    public static int bestLineScore;

    public static Status bestLineScoreSuccess = Status.NONE;

    public static String guid;

    public static int bestUserColorScore;

    public static int bestUserDigitScore;

    public static int bestUserLineScore;

    public static void updateUserScores(TUser user) {
        if(user == null) {
            return;
        }
        if(user.getBest_color_score() > bestUserColorScore) {
            bestUserColorScore = user.getBest_color_score();
        }
        if(user.getBest_digit_score() > bestUserDigitScore) {
            bestUserDigitScore = user.getBest_digit_score();
        }
        if(user.getBest_line_score() > bestUserLineScore) {
            bestUserLineScore = user.getBest_line_score();
        }
    }

    public static void updateBestScores(TUser user) {
        if (user == null) {
            return;
        }
        if(bestUserColorScore > bestColorScore) {
            bestColorScore = bestUserColorScore;
        }
        if(bestUserDigitScore > bestDigitScore) {
            bestDigitScore = bestUserDigitScore;
        }
        if(bestUserLineScore > bestLineScore) {
            bestLineScore = bestUserLineScore;
        }
    }

    public static void updateCacheSp(Context context) {
        SharePreferencesManager sp = SharePreferencesManager.getInstance();

        //sp.putInt(context, SpConfig.sBestColorScore, ScoresManager.bestColorScore);
        //sp.putInt(context, SpConfig.sBestDigitScore, ScoresManager.bestDigitScore);
       // sp.putInt(context, SpConfig.sBestLineScore, ScoresManager.bestLineScore);

        sp.putInt(context, SpConfig.sColorScore, ScoresManager.bestUserColorScore);
        sp.putInt(context, SpConfig.sDigitScore, ScoresManager.bestUserDigitScore);
        sp.putInt(context, SpConfig.sLineScore, ScoresManager.bestUserLineScore);
    }

}
