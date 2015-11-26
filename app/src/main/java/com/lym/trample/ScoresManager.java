package com.lym.trample;

import android.content.Context;

import com.lym.trample.bean.TUser;
import com.lym.trample.conf.SpConfig;
import com.lym.trample.utils.SharePreferencesManager;

import java.util.UUID;

/**
 * Created by mao on 2015/11/24.
 */
public class ScoresManager {

    /** 总用户数 */
    public static int totalUserCount;

    /** 踩颜色最高分 */
    public static int bestColorScore;

    /** 踩数字最高分 */
    public static int bestDigitScore;

    /** 踩边线最高分 */
    public static int bestLineScore;


    /***
     * 个人相关
     */

    public static String guid;

    /** 我的踩颜色历史最高分 */
    public static int bestUserColorScore;

    /** 我的踩数字历史最高分 */
    public static int bestUserDigitScore;

    /** 我的踩边线历史最高分 */
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

        sp.putInt(context, SpConfig.sBestColorScore, ScoresManager.bestColorScore);
        sp.putInt(context, SpConfig.sBestDigitScore, ScoresManager.bestDigitScore);
        sp.putInt(context, SpConfig.sBestLineScore, ScoresManager.bestLineScore);

        sp.getInt(context, SpConfig.sColorScore, ScoresManager.bestUserColorScore);
        sp.getInt(context, SpConfig.sDigitScore, ScoresManager.bestUserDigitScore);
        sp.getInt(context, SpConfig.sLineScore, ScoresManager.bestUserLineScore);
    }

}
